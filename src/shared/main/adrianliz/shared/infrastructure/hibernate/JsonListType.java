package adrianliz.shared.infrastructure.hibernate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

public class JsonListType implements UserType, DynamicParameterizedType {

  private static final int[] SQL_TYPES = new int[] {Types.LONGVARCHAR};
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private JavaType valueType = null;
  private Class<?> classType = null;

  @Override
  public int[] sqlTypes() {
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return classType;
  }

  @Override
  public boolean equals(final Object x, final Object y) throws HibernateException {
    return Objects.equals(x, y);
  }

  @Override
  public int hashCode(final Object x) throws HibernateException {
    return Objects.hashCode(x);
  }

  @Override
  public Object nullSafeGet(
      final ResultSet rs,
      final String[] names,
      final SharedSessionContractImplementor session,
      final Object owner)
      throws HibernateException, SQLException {
    return nullSafeGet(rs, names, owner);
  }

  @Override
  public void nullSafeSet(
      final PreparedStatement st,
      final Object value,
      final int index,
      final SharedSessionContractImplementor session)
      throws HibernateException, SQLException {
    nullSafeSet(st, value, index);
  }

  @Override
  public Object deepCopy(final Object value) throws HibernateException {
    if (value == null) {
      return null;
    } else if (valueType.isCollectionLikeType()) {
      return Lists.newArrayList(value);
    }

    return null;
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Serializable disassemble(final Object value) throws HibernateException {
    return (Serializable) deepCopy(value);
  }

  @Override
  public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
    return deepCopy(cached);
  }

  @Override
  public Object replace(final Object original, final Object target, final Object owner)
      throws HibernateException {
    return deepCopy(original);
  }

  @Override
  public void setParameterValues(final Properties parameters) {
    try {
      final Class<?> entityClass = Class.forName(parameters.getProperty("list_of"));

      valueType =
          OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, entityClass);
      classType = List.class;
    } catch (final ClassNotFoundException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner)
      throws HibernateException, SQLException {
    final String value =
        rs.getString(names[0]).replace("\"value\"", "").replace("{:", "").replace("}", "");
    Object result = null;

    if (valueType == null) {
      throw new HibernateException("Value type not set.");
    }

    if (!value.equals("")) {
      try {
        result = OBJECT_MAPPER.readValue(value, valueType);
      } catch (final IOException e) {
        throw new HibernateException("Exception deserializing value " + value, e);
      }
    }

    return result;
  }

  public void nullSafeSet(final PreparedStatement st, final Object value, final int index)
      throws HibernateException, SQLException {
    final StringWriter sw = new StringWriter();
    OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    if (value == null) {
      st.setNull(index, Types.VARCHAR);
    } else {
      try {
        OBJECT_MAPPER.writeValue(sw, value);
        st.setString(index, sw.toString());
      } catch (final IOException e) {
        throw new HibernateException("Exception serializing value " + value, e);
      }
    }
  }
}
