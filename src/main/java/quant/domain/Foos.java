package quant.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Foos {

	@JsonIgnore
	private Map<String, List<Foo>> properties = new HashMap<String, List<Foo>>();

	@JsonAnyGetter
	public Map<String, List<Foo>> getProperties() {
	return this.properties;
	}

	@JsonAnySetter
	public void setProperty(String name, List<Foo> value) {
	this.properties.put(name, value);
	}

	}