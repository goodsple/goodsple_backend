package com.goodsple.typehandler;

import com.goodsple.features.auth.enums.Gender;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(Gender.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class GenderTypeHandler extends BaseTypeHandler<Gender> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Gender parameter, JdbcType jdbcType)
            throws SQLException {
        // Java enum MALE → DB enum 'male'
        ps.setObject(i, parameter.name().toLowerCase(), java.sql.Types.OTHER);
    }

    @Override
    public Gender getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String val = rs.getString(columnName);
        return val == null ? null : Gender.valueOf(val.toUpperCase());
    }

    @Override
    public Gender getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String val = rs.getString(columnIndex);
        return val == null ? null : Gender.valueOf(val.toUpperCase());
    }

    @Override
    public Gender getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String val = cs.getString(columnIndex);
        return val == null ? null : Gender.valueOf(val.toUpperCase());
    }

}
