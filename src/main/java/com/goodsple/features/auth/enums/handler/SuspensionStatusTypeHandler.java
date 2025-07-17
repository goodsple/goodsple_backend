package com.goodsple.features.auth.enums.handler;

import com.goodsple.features.auth.enums.LoginType;
import com.goodsple.features.auth.enums.Role;
import com.goodsple.features.auth.enums.SuspensionStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

@MappedTypes(SuspensionStatus.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class SuspensionStatusTypeHandler extends BaseTypeHandler<SuspensionStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    SuspensionStatus parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter.name().toLowerCase(), Types.OTHER);
    }

    @Override
    public SuspensionStatus getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        String val = rs.getString(columnName);
        return val == null ? null : SuspensionStatus.valueOf(val.toUpperCase());
    }

    @Override
    public SuspensionStatus getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        String val = rs.getString(columnIndex);
        return val == null ? null : SuspensionStatus.valueOf(val.toUpperCase());
    }

    @Override
    public SuspensionStatus getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        String val = cs.getString(columnIndex);
        return val == null ? null : SuspensionStatus.valueOf(val.toUpperCase());
    }
}
