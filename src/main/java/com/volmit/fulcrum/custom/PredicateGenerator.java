package com.volmit.fulcrum.custom;

import org.bukkit.Material;

import com.volmit.fulcrum.lang.F;
import com.volmit.fulcrum.lang.GList;
import com.volmit.fulcrum.lang.GMap;
import com.volmit.fulcrum.lang.JSONArray;
import com.volmit.fulcrum.lang.JSONObject;

public class PredicateGenerator
{
	private Material type;
	private String model;
	private GList<String> models;
	private int max;
	private JSONArray overrides;
	private JSONObject parenter;
	private GMap<String, Integer> mapping;

	public PredicateGenerator(Material type, String model) throws UnsupportedOperationException
	{
		models = new GList<String>();
		this.model = model;
		max = type.getMaxDurability();
		overrides = new JSONArray();
		parenter = new JSONObject();
		parenter.put("parent", model);
		mapping = new GMap<String, Integer>();

		if(max == 0)
		{
			throw new UnsupportedOperationException("The type " + type.toString() + " does not support durability predacates");
		}
	}

	public Material getType()
	{
		return type;
	}

	public String getModel()
	{
		return model;
	}

	public GList<String> getModels()
	{
		return models;
	}

	public int getMax()
	{
		return max;
	}

	public JSONArray getOverrides()
	{
		return overrides;
	}

	public GMap<String, Integer> getMapping()
	{
		return mapping;
	}

	public String modelSuperName()
	{
		return model + "_super";
	}

	public JSONObject generateModel(String texture)
	{
		JSONObject textures = new JSONObject();
		textures.put("layer0", texture);
		JSONObject object = new JSONObject();
		object.put("parent", "item/handheld");
		object.put("textures", textures);
		object.put("overrides", getOverrides());
		return object;
	}

	public void generate()
	{
		overrides = new JSONArray();
		JSONObject basePred = new JSONObject();
		JSONObject br = new JSONObject();
		br.put("damaged", 1);
		br.put("damage", 0);
		basePred.put("predicate", br);
		basePred.put("model", model);
		GList<String> pending = models.copy();
		GMap<String, Integer> mapping = new GMap<String, Integer>();

		for(int i = 0; i < max; i++)
		{
			boolean put = !pending.isEmpty();
			String m = pending.isEmpty() ? modelSuperName() : pending.pop();
			double raw = (double) ((float) ((double) i / (double) max));
			raw = Double.valueOf(F.f(raw, 10));
			JSONObject pred = new JSONObject();
			JSONObject brx = new JSONObject();
			brx.put("damaged", 0);
			brx.put("damage", raw);
			pred.put("predicate", brx);
			pred.put("model", m);

			if(put)
			{
				mapping.put(m, i);
			}

			overrides.put(pred);
		}

		overrides.put(basePred);
	}

	public JSONObject getParenter()
	{
		return parenter;
	}
}
