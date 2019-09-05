package com.example.filght.demo.enums;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum OrderEnum {
	ASC("ASC"), DESC("DESC");

	private static final Map<String, OrderEnum> BY_LABEL = new HashMap<>();

	// Static Block
	static {
		for (OrderEnum e : values()) {
			BY_LABEL.put(e.label, e);
		}

	}
	// Fields
	private final String label;

	// Constructor
	OrderEnum(String label) {
		this.label = label;
	}
	
	// Methods
		public static OrderEnum valueOfLabel(String label) {
			return BY_LABEL.get(label.toUpperCase(Locale.getDefault()));
		}

		public static boolean equals(String orderBy) {
			for (OrderEnum orderEnum : values()) {
				if (orderBy.equalsIgnoreCase(orderEnum.label)) {
					return true;
				}
			}
			return false;
		}

		public String toString() {
			return this.label;
		}
}
