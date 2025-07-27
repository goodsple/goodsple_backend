package com.goodsple.typehandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.*;

import java.sql.*;
import java.time.LocalDate;

@MappedTypes(LocalDate.class)            // Java 측 타입
@MappedJdbcTypes({JdbcType.DATE, JdbcType.OTHER})
@Slf4j
public class LocalDateDomainTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    LocalDate parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setDate(i, Date.valueOf(parameter));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        log.debug("handler(String): column={} raw={}", columnName, rs.getObject(columnName));
        Date d = rs.getDate(columnName);
        return (d != null ? d.toLocalDate() : null);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        log.debug("handler(int): index={} raw={}", columnIndex, rs.getObject(columnIndex));
        Date d = rs.getDate(columnIndex);
        return (d != null ? d.toLocalDate() : null);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        log.debug("handler(callable): index={} raw={}", columnIndex, cs.getObject(columnIndex));
        Date d = cs.getDate(columnIndex);
        return (d != null ? d.toLocalDate() : null);
    }
}
