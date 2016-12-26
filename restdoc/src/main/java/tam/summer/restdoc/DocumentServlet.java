package tam.summer.restdoc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import tam.summer.common.StringUtil;
import tam.summer.restdoc.service.MetaService;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;

/**
 * Created by tanqimin on 2015/11/14.
 */
@WebServlet(value = "/doc/*")
public class DocumentServlet extends DocumentResourceServlet {

    private MetaService metaService = null;

    @Override
    protected String process(String url) throws IOException {
        if (metaService == null)
            metaService = new MetaService(getServletContext());

        //根据URL处理JSON
        if (url.equals("/module.json")) {
            //处理获取模块JSON
            return JSON.toJSONString(metaService.getApiModules(), SerializerFeature.DisableCircularReferenceDetect);
        } else if (StringUtil.surroundWith(url, "/api/", ".json")) {
            //处理获取API类的JSON
            String clazzName = StringUtil.remove(url, "/api/", ".json");
            return JSON.toJSONString(metaService.getApiClass(clazzName), SerializerFeature.DisableCircularReferenceDetect);
        } else if (StringUtil.surroundWith(url, "/method/", ".json")) {
            //处理获取API方法JSON
            String[] pathVar = StringUtil.remove(url, "/method/", ".json").split("/");
            if (pathVar.length == 2) {
                String className = pathVar[0];
                String methodName = pathVar[1];
                return JSON.toJSONString(metaService.getMethod(className, methodName), SerializerFeature.DisableCircularReferenceDetect);
            }
        } else if (StringUtil.surroundWith(url, "/class/", ".json")) {
            //处理获取API类或API类方法的JSON
            String clazzName = StringUtil.remove(url, "/class/", ".json");
            return JSON.toJSONString(metaService.getModelClass(clazzName), SerializerFeature.DisableCircularReferenceDetect);
        } else if (url.equals("/config.json")) {
            return JSON.toJSONString(metaService.getConfig(), SerializerFeature.DisableCircularReferenceDetect);
        }
        return null;
    }

}
