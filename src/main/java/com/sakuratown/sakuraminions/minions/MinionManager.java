package com.sakuratown.sakuraminions.minions;

import java.util.ArrayList;
import java.util.HashMap;

public final class MinionManager {
    private HashMap<String, Minion> minionList = new HashMap<>();
    private static MinionManager instance = new MinionManager();

    private MinionManager() {
    }

    public static MinionManager getInstance() {
        if (instance == null) {
            instance = new MinionManager();
        }
        return instance;
    }

    public void addMinion(String uuid, Minion minion) {
        minionList.put(uuid, minion);
    }

    public void removeMinion(String uuid) {
        minionList.remove(uuid);
    }

    public boolean isMinion(String uuid) {
        return minionList.containsKey(uuid);
    }

    public Minion getMinion(String uuid) {
        return minionList.get(uuid);
    }

    public void removeAll() {
        minionList.clear();
    }
    public ArrayList<String> getAllUUID() {
        return new ArrayList<>(minionList.keySet());
    }


}
