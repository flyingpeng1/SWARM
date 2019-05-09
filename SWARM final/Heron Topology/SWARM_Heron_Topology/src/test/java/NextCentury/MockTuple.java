package NextCentury;

import java.util.ArrayList;
import java.util.List;

import com.twitter.heron.api.generated.TopologyAPI.StreamId;
import com.twitter.heron.api.tuple.Fields;
import com.twitter.heron.api.tuple.Tuple;

public class MockTuple implements Tuple {

	@Override
	public boolean contains(String field) {
		return false;
	}

	@Override
	public int fieldIndex(String field) {
		return 0;
	}

	@Override
	public byte[] getBinary(int i) {
		return null;
	}

	@Override
	public byte[] getBinaryByField(String field) {
		return null;
	}

	@Override
	public Boolean getBoolean(int i) {
		return null;
	}

	@Override
	public Boolean getBooleanByField(String field) {
		return null;
	}

	@Override
	public Byte getByte(int i) {
		return null;
	}

	@Override
	public Byte getByteByField(String field) {
		return null;
	}

	@Override
	public Double getDouble(int i) {
		return null;
	}

	@Override
	public Double getDoubleByField(String field) {
		return null;
	}

	@Override
	public Fields getFields() {
		return new Fields("Val1","Val2","Val3","Val4","Val5","Val6");
	}

	@Override
	public Float getFloat(int i) {
		return null;
	}

	@Override
	public Float getFloatByField(String field) {
		return null;
	}

	@Override
	public Integer getInteger(int i) {
		return null;
	}

	@Override
	public Integer getIntegerByField(String field) {
		return null;
	}

	@Override
	public Long getLong(int i) {
		return null;
	}

	@Override
	public Long getLongByField(String field) {
		return null;
	}

	@Override
	public Short getShort(int i) {
		return null;
	}

	@Override
	public Short getShortByField(String field) {
		return null;
	}

	@Override
	public String getSourceComponent() {
		return null;
	}

	@Override
	public StreamId getSourceGlobalStreamId() {
		return null;
	}

	@Override
	public String getSourceStreamId() {
		return null;
	}

	@Override
	public int getSourceTask() {
		return 0;
	}

	@Override
	public String getString(int i) {
		return null;
	}

	@Override
	public String getStringByField(String field) {
		return null;
	}

	@Override
	public Object getValue(int i) {
		return null;
	}

	@Override
	public Object getValueByField(String field) {
		return null;
	}

	@Override
	public List<Object> getValues() {
		ArrayList<Object> l = new ArrayList<Object>();
		l.add("test");
		l.add("test2");
		l.add(new Double(10.123));
		l.add("test4");
		l.add("test5");
		l.add(new Double(2.1));
		return l;
	}

	@Override
	public void resetValues() {
	}

	@Override
	public List<Object> select(Fields selector) {
		return null;
	}

	@Override
	public int size() {
		return 6;
	}
}
