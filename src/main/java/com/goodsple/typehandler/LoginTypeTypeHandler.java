package com.goodsple.typehandler;


import com.goodsple.features.auth.enums.LoginType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(LoginType.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class LoginTypeTypeHandler extends BaseTypeHandler<LoginType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    LoginType parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter.name().toLowerCase(), Types.OTHER);
    }
    @Override
    public LoginType getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String val = rs.getString(columnName);
        return val == null ? null : LoginType.valueOf(val.toUpperCase());
    }

    @Override
    public LoginType getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String val = rs.getString(columnIndex);
        return val == null ? null : LoginType.valueOf(val.toUpperCase());
    }

    @Override
    public LoginType getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String val = cs.getString(columnIndex);
        return val == null ? null : LoginType.valueOf(val.toUpperCase());
    }
}
