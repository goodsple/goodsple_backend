package com.goodsple.features.auth.enums.handler;

import com.goodsple.features.auth.enums.Role;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.sql.Types;

@MappedTypes(Role.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class RoleTypeHandler extends BaseTypeHandler<Role> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Role parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter.name().toLowerCase(), Types.OTHER);
    }

    @Override
    public Role getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String val = rs.getString(columnName);
        return val == null ? null : Role.valueOf(val.toUpperCase());
    }

    @Override
    public Role getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String val = rs.getString(columnIndex);
        return val == null ? null : Role.valueOf(val.toUpperCase());
    }

    @Override
    public Role getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String val = cs.getString(columnIndex);
        return val == null ? null : Role.valueOf(val.toUpperCase());
    }
}
