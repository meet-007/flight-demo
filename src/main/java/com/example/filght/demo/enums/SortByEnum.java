package com.example.filght.demo.enums;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public enum SortByEnum {
	arrivalTime("arrivalTime"), departure("departure"), arrival("arrival"), departureTime("departureTime");

	private static final Map<String, SortByEnum> BY_LABEL = new HashMap<>();

	// Static Block
	static {
		for (SortByEnum e : values()) {
			BY_LABEL.put(e.label, e);
		}

	}
	// Fields
	private final String label;

	// Constructor
	SortByEnum(String label) {
		this.label = label;
	}
	
	// Methods
		public static SortByEnum valueOfLabel(String label) {
			return BY_LABEL.get(label.toUpperCase(Locale.getDefault()));
		}

		public static boolean equals(String sortBy) {
			for (SortByEnum sortByEnum : values()) {
				if (sortBy.equalsIgnoreCase(sortByEnum.label)) {
					return true;
				}
			}
			return false;
		}

		public String toString() {
			return this.label;
		}

}
