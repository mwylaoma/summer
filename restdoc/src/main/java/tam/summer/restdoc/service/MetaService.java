package tam.summer.restdoc.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tam.summer.common.ValidateUtil;
import tam.summer.common.reflect.BaseTypes;
import tam.summer.common.reflect.ReflectUtil;
import tam.summer.restdoc.annotation.*;
import tam.summer.restdoc.common.PropertyUtil;
import tam.summer.restdoc.meta.*;

import javax.servlet.ServletContext;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by tanqimin on 2015/11/19.
 */
public class MetaService {

    private static       Logger                  logger               = LogManager.getLogger(MetaService.class);
    private static       Set<Class<?>>           apiControllerClasses = null;    //API Controller Classes
    private static       List<ApiModule>         apiModules           = null;
    private final static Map<String, ModelClass> modelClasses         = new HashMap<>();
    private final static Map<String, ApiClass>   apiClasses           = new HashMap<>();

    private ServletContext servletContext;
    private String         apiPackage;//API Controller package 路径

    public MetaService(ServletContext servletContext) {
        this.servletContext = servletContext;
        PropertyUtil.createInstance(this.servletContext);
        apiPackage = PropertyUtil.getProperties().getProperty("doc.api.package");
    }

    private static List<ApiMethod> getApiMethods(Class<?> restApiClass) {
        List<ApiMethod> apiMethods = new ArrayList<>();

        MethodComment  methodComment;
        RequestMapping methodRequestMapping;
        RequestMapping apiRequestMapping = restApiClass.getAnnotation(RequestMapping.class);
        String         apiUrl;
        String         methodUrl;
        String         httpMethod;
        ApiMethod      apiMethod;

        for (Method method : restApiClass.getDeclaredMethods()) {
            methodComment = method.getAnnotation(MethodComment.class);
            if (methodComment == null) continue;

            methodRequestMapping = method.getAnnotation(RequestMapping.class);
            if (methodRequestMapping == null) continue;

            apiUrl = (apiRequestMapping == null || apiRequestMapping.value().length == 0) ? "" : apiRequestMapping
                    .value()[0].toString();
            methodUrl = (methodRequestMapping.value().length == 0) ? "" : methodRequestMapping.value()[0].toString();
            httpMethod = (methodRequestMapping.method().length == 0) ? "" : methodRequestMapping.method()[0].toString();

            apiMethod = new ApiMethod();
            apiMethod.setName(methodComment.value());
            apiMethod.setMethodName(method.getName());
            apiMethod.setDescription(methodComment.description());
            apiMethod.setRequireAuth(methodComment.isAuth());
            apiMethod.setHttpMethod(httpMethod);
            apiMethod.setUrl(apiUrl.concat(methodUrl));
            apiMethod.setParams(getApiParams(method));
            ModelClass apiReturn = getModelClass(method.getGenericReturnType());
            apiMethod.setApiReturn(apiReturn);
            if (apiReturn != null && BaseTypes.isBaseType(apiReturn.getType()) == false)
                apiMethod.setApiReturnShowLinked(true);

            apiMethods.add(apiMethod);
        }
        return apiMethods;
    }

    /**
     * 获取指定类型的类对象及其成员
     *
     * @param returnType
     * @return
     */
    public static ModelClass getModelClass(Type returnType) {
        ModelClass returnApiClass = null;
        if (returnType != null && returnType.getTypeName().equals("org.springframework.http.ResponseEntity") == false) {
            returnType = getActualType(returnType);
            Class<?> returnClass = (Class<?>) returnType;

            if (modelClasses.containsKey(returnClass.getName())) return modelClasses.get(returnClass.getName());

            ClassComment returnClsComment = returnClass.getAnnotation(ClassComment.class);

            String returnClsName = returnClsComment == null ? returnClass.getSimpleName() : returnClsComment.value();
            String returnClsDesc = returnClsComment == null ? returnClass.getName() : returnClsComment.description();

            returnApiClass = new ModelClass();
            returnApiClass.setName(returnClsName);
            returnApiClass.setDescription(returnClsDesc);
            returnApiClass.setType(returnClass);
            returnApiClass.setTypeName(returnClass.getSimpleName());
            returnApiClass.setModelFields(getModelFields(returnClass));

            modelClasses.put(returnClass.getName(), returnApiClass);
        }
        return returnApiClass;
    }

    private static List<ModelField> getModelFields(Class<?> clazz) {
        List<ModelField> result = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            FieldComment fieldComment = field.getAnnotation(FieldComment.class);
            if (fieldComment == null) continue;
            Class<?> fieldClass = getActualType(field.getType());

            ModelField modelField = new ModelField(field.getName(), fieldComment.description(), fieldComment
                    .required());
            modelField.setType(fieldClass);
            modelField.setTypeName(fieldClass.getSimpleName());
            modelField.setShowLinked(BaseTypes.isBaseType(fieldClass) == false);
            result.add(modelField);
        }
        return result;
    }

    /**
     * 从方法中的参数获取API 参数
     *
     * @param method
     * @return
     */
    private static List<ApiParam> getApiParams(Method method) {
        List<ApiParam> results = new ArrayList<>();
        ParamComment   paramComment;
        ApiParam       apiParam;
        for (Parameter parameter : method.getParameters()) {
            paramComment = parameter.getAnnotation(ParamComment.class);
            if (paramComment == null) continue;

            apiParam = new ApiParam();
            apiParam.setName(paramComment.value());
            apiParam.setDescription(paramComment.description());
            apiParam.setIsPathVariable(parameter.getAnnotation(PathVariable.class) != null);
            Class<?> paramType = getActualType(parameter.getParameterizedType());
            apiParam.setType(paramType);
            apiParam.setTypeName(paramType.getSimpleName());
            apiParam.setShowLinked(BaseTypes.isBaseType(apiParam.getType()) == false);
            ModelClass modelClass = getModelClass(apiParam.getType());
            apiParam.setModelClass(modelClass);
            results.add(apiParam);

            //获取参数类结构并放入缓存
            getModelClass(parameter.getParameterizedType());
        }
        return results;
    }

    /**
     * 获取泛型类型或数组的实际类型（递归）
     *
     * @param type
     * @return
     */
    private static Class<?> getActualType(Type type) {
        if (type instanceof ParameterizedType) {
            Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
            return getActualType(actualType);
        }
        Class<?> typeClass = (Class<?>) type;
        if (typeClass.isArray()) {
            Type actualType = typeClass.getComponentType();
            return getActualType(actualType);
        }
        return typeClass;
    }

    public List<ApiModule> getApiModules() {
        if (apiModules == null) {
            apiModules = new ArrayList<>();
            apiControllerClasses = ReflectUtil.loadPackage(apiPackage);
            for (Class<?> restApiClass : apiControllerClasses) {
                RestController restController = restApiClass.getAnnotation(RestController.class);
                if (restController == null) continue;

                ClassComment classComment = restApiClass.getAnnotation(ClassComment.class);
                if (classComment == null) continue;

                ApiModule apiModule = null;
                ApiClass  apiClass  = new ApiClass();

                apiClass.setName(classComment.value());
                apiClass.setDescription(classComment.description());
                apiClass.setType(restApiClass);

                ModuleComment moduleComment = restApiClass.getAnnotation(ModuleComment.class);
                if (moduleComment != null) {
                    for (ApiModule module : apiModules) {
                        if (ValidateUtil.equalsIgnoreCase(module.getName(), moduleComment.value())) {
                            apiModule = module;
                            break;
                        }
                    }

                    if (apiModule == null) {
                        apiModule = new ApiModule(moduleComment.value(), moduleComment.sortIndex());
                        apiModules.add(apiModule);
                    }

                    apiClass.setApiModule(apiModule);
                    apiModule.getApiClasses().add(apiClass);
                }
            }
        }
        return apiModules;
    }

    public ApiClass getApiClass(String clazzName) {
        String clsName = clazzName.toUpperCase();
        if (apiClasses.containsKey(clsName)) return apiClasses.get(clsName);

        ApiClass apiClass = null;
        for (ApiModule apiModule : getApiModules()) {
            List<ApiClass> apiClasses = apiModule.getApiClasses();
            for (ApiClass api : apiClasses) {
                if (ValidateUtil.equalsIgnoreCase(api.getType().getName(), clazzName)) {
                    apiClass = api;
                    break;
                }
            }
        }
        if (apiClass == null) throw new RuntimeException("Can not found the api controller class named :" + clazzName);

        apiClass.setApiMethods(getApiMethods(apiClass.getType()));
        apiClasses.put(clsName, apiClass);
        return apiClass;
    }

    public ApiMethod getMethod(String className, String methodName) {
        ApiClass  apiClass  = getApiClass(className);
        ApiMethod apiMethod = null;
        for (ApiMethod method : apiClass.getApiMethods()) {
            if (ValidateUtil.equalsIgnoreCase(methodName, method.getMethodName())) {
                apiMethod = method;
                break;
            }
        }
        return apiMethod;
    }

    public ModelClass getModelClass(String clazzName) {
        Class<?> modelClass = null;
        try {
            modelClass = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class " + clazzName + " not found");
        }

        return getModelClass(modelClass);
    }

    public Map<String, String> getConfig() {
        Map<String, String> result = new HashMap<>();
        result.put("global.product", PropertyUtil.getProperty("global.product"));
        result.put("global.company", PropertyUtil.getProperty("global.company"));
        result.put("global.description", PropertyUtil.getProperty("global.description"));
        return result;
    }
}
