package com.itechart.test.altir.receiver.service.mapper;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

public class BatchPreparedStatementSetterExecCont implements BatchPreparedStatementSetter {
	private final Map<Long, String> serializedContexts;
	private final Iterator<Long> executionIdIterator;

	private static final int DEFAULT_MAX_VARCHAR_LENGTH = 2500;
	private int shortContextLength = DEFAULT_MAX_VARCHAR_LENGTH;
	private LobHandler lobHandler = new DefaultLobHandler();

	public BatchPreparedStatementSetterExecCont(Map<Long, String> serializedContexts) {
		this.serializedContexts = serializedContexts;
		executionIdIterator = serializedContexts.keySet().iterator();

	}

	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		Long executionId = executionIdIterator.next();
		String serializedContext = serializedContexts.get(executionId);
		String shortContext;
		String longContext;
		if (serializedContext.length() > shortContextLength) {
			// Overestimate length of ellipsis to be on the safe side with
			// 2-byte chars
			shortContext = serializedContext.substring(0, shortContextLength - 8) + " ...";
			longContext = serializedContext;
		} else {
			shortContext = serializedContext;
			longContext = null;
		}
		ps.setString(1, shortContext);
		if (longContext != null) {
			lobHandler.getLobCreator().setClobAsString(ps, 2, longContext);
		} else {
			ps.setNull(2, Types.CLOB);
		}
		ps.setLong(3, executionId);
	}

	@Override
	public int getBatchSize() {
		return serializedContexts.size();
	}
}
