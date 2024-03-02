package dbprak20.user_types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;

public class PathArrayUserType implements UserType {

    protected static final int SQLTYPE = Types.ARRAY;

    private long[] toPrimitive(Long[] array) {
        int length = array.length;
        long[] out = new long[length];
        for (int i = 0; i < length; i++) {
            out[i] = array[i];
        }

        return out;
    }

    private Long[] toObject(long[] array) {
        int length = array.length;
        Long[] out = new Long[length];
        for (int i = 0; i < length; i++) {
            out[i] = array[i];
        }

        return out;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] { SQLTYPE };
    }

    @Override
    public Class<Long[]> returnedClass() {
        return Long[].class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return o == null ? o1 == null : o.equals(o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o == null ? 0 : o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor sharedSessionContractImplementor, Object owner) throws HibernateException, SQLException {
        Array array = rs.getArray(names[0]);
        if (array == null) {
            return null;
        }
        Long[] pathArray = (Long[]) array.getArray();
        return toPrimitive(pathArray);
    }

    @Override
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        Connection connection = statement.getConnection();

        if (value == null) {
            statement.setNull(index, sqlTypes()[0]);
        } else {
            long[] castObject = (long[]) value;
            Long[] object = toObject(castObject);
            Array array = connection.createArrayOf("long", object);

            statement.setArray(index, array);
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        return o == null ? null : ((Long[]) o).clone();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) o;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
