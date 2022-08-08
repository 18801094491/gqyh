package com.zcdy.dsc.common.framework.spring.propertyeditor;

/**
 * @author Roberto
 * @date 2020/05/15
 */
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * Property editor for {@code java.util.Date}, supporting a custom {@code java.text.DateFormat}.
 *
 * <p>
 * This is not meant to be used as system PropertyEditor but rather as locale-specific date editor within custom
 * controller code, parsing user-entered number strings into Date properties of beans and rendering them in the UI form.
 *
 * <p>
 * In web MVC code, this editor will typically be registered with {@code binder.registerCustomEditor}.
 *
 * @author Juergen Hoeller
 * @since 28.04.2003
 * @see java.util.Date
 * @see java.text.DateFormat
 * @see org.springframework.validation.DataBinder#registerCustomEditor
 */
public class CustomDateEditor extends PropertyEditorSupport {

    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final DateFormat dateTime1Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final boolean allowEmpty;

    private final int exactDateLength;

    /**
     * Create a new CustomDateEditor instance, using the given DateFormat for parsing and rendering.
     * <p>
     * The "allowEmpty" parameter states if an empty String should be allowed for parsing, i.e. get interpreted as null
     * value. Otherwise, an IllegalArgumentException gets thrown in that case.
     * 
     * @param dateFormat
     *            the DateFormat to use for parsing and rendering
     * @param allowEmpty
     *            if empty strings should be allowed
     */
    public CustomDateEditor(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(@Nullable String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        } else {
            try {
                setValue(this.dateTimeFormat.parse(text));
            } catch (ParseException ex) {
                try {
                    setValue(this.dateTime1Format.parse(text));
                } catch (Exception e1) {
                    try {
                        setValue(this.dateFormat.parse(text));
                    } catch (Exception e2) {
                        throw new IllegalArgumentException("Could not parse date: " + e2.getMessage(), e2);
                    }
                }
            }
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Date value = (Date)getValue();
        return (value != null ? this.dateTimeFormat.format(value) : "");
    }

}
