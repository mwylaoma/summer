/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tam.summer.database.persistence.handler.impls;

import tam.summer.database.persistence.handler.PropertyHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;

public class SQLXMLColumnHandler
        implements PropertyHandler {
    @Override
    public boolean match(Class<?> propType) {
        return propType.equals(SQLXML.class);
    }

    @Override
    public Object apply(
            ResultSet rs,
            String columnName)
            throws SQLException {
        return rs.getSQLXML(columnName);
    }

    @Override
    public void refer(
            PreparedStatement ps,
            int paramIndex,
            Object param)
            throws SQLException {
        ps.setSQLXML(paramIndex, (SQLXML) param);
    }
}
