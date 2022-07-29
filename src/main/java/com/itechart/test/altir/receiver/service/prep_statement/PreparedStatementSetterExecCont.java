package com.itechart.test.altir.receiver.service.prep_statement;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PreparedStatementSetterExecCont implements PreparedStatementSetter {
	private static final int DEFAULT_MAX_VARCHAR_LENGTH = 2500;
	private int shortContextLength = DEFAULT_MAX_VARCHAR_LENGTH;

	private final String shortContext;
	private final String longContext;
	private LobHandler lobHandler = new DefaultLobHandler();
	private Long executionId;

	public PreparedStatementSetterExecCont(String serializedContext, Long executionId) {
		this.executionId = executionId;
		if (serializedContext.length() > shortContextLength) {
			// Overestimate length of ellipsis to be on the safe side with
			// 2-byte chars
			shortContext = serializedContext.substring(0, shortContextLength - 8) + " ...";
			longContext = serializedContext;
		} else {
			shortContext = serializedContext;
			longContext = null;
		}
	}

	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
		ps.setString(1, shortContext);
		if (longContext != null) {
			lobHandler.getLobCreator().setClobAsString(ps, 2, longContext);
		} else {
			ps.setNull(2, Types.CLOB);
		}
		ps.setLong(3, executionId);
	}
}
