package com.example.newjapaneseapp;

import java.util.HashMap;

public class ParticleMemory {
    private static final ParticleMemory ourInstance = new ParticleMemory();
    private String particle;
    private HashMap<String, Integer> jsonMap = new HashMap<>();

    public static ParticleMemory getInstance() {
        return ourInstance;
    }

    private ParticleMemory() {
        jsonMap.put("が", R.raw.ga_particle_data);
        jsonMap.put("の", R.raw.no_particle_data);
        jsonMap.put("で", R.raw.de_particle_data);
        jsonMap.put("だ, です", R.raw.da_desu_particle_data);
        jsonMap.put("に", R.raw.ni_particle_data);
        jsonMap.put("と", R.raw.to_particle_data);
    }

    public String getParticle() {
        return particle;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public HashMap<String, Integer> getJsonMap() {
        return jsonMap;
    }

    public void setJsonMap(HashMap<String, Integer> jsonMap) {
        this.jsonMap = jsonMap;
    }
}
