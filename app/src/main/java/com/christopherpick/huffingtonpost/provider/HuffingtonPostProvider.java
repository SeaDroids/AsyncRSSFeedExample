package com.christopherpick.huffingtonpost.provider;

import com.christopherpick.huffingtonpost.base.BaseLookup;
import com.christopherpick.huffingtonpost.base.BaseProvider;
import com.christopherpick.huffingtonpost.tables.HuffingtonPostTable;

public class HuffingtonPostProvider extends BaseProvider {

		@Override
		protected String getDatabaseName() {
			return "huffingtonpost.db";
		}

		@Override
		protected int getDatabaseVersion() {
			return 1;
		}

		@Override
		protected void addTables(BaseLookup locator) {
			locator.putTable(new HuffingtonPostTable());
		}
	}