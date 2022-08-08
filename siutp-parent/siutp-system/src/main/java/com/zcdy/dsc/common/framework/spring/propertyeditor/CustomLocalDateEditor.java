package com.zcdy.dsc.common.framework.spring.propertyeditor;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author: Roberto
 * 创建时间:2020年4月26日 上午10:33:45
 * 描述: <p></p>
 */
public class CustomLocalDateEditor extends PropertyEditorSupport{

	private final DateTimeFormatter dateTimeFormatter;
	
	private final boolean allowEmpty;
	
	private final int exactDateLength;
	
	public CustomLocalDateEditor(DateTimeFormatter dateTimeFormatter, boolean allowEmpty, int exactDateLength) {
		this.allowEmpty = allowEmpty;
		this.exactDateLength = exactDateLength;
		this.dateTimeFormatter = dateTimeFormatter;
	}
	
	public CustomLocalDateEditor(DateTimeFormatter dateTimeFormatter, boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
		this.exactDateLength = -1;
		this.dateTimeFormatter = dateTimeFormatter;
	}
	
	@Override
	public void setAsText(@Nullable String text) throws IllegalArgumentException, DateTimeParseException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		}
		else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
			throw new IllegalArgumentException(
					"Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
		}
		else {
			setValue(LocalDate.from(this.dateTimeFormatter.parse(text)));
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		LocalDate value = (LocalDate) getValue();
		return (value != null ? value.format(dateTimeFormatter) : "");
	}
}
