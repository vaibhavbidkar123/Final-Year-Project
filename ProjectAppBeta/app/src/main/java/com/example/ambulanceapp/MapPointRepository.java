package com.example.ambulanceapp;

import java.util.List;

public class MapPointRepository {

    private MapPointDao mapPointDao;

    public MapPointRepository(MapPointDao mapPointDao) {
        this.mapPointDao = mapPointDao;
    }

//    public Users login(String name, String password) {
//        return userDao.login(name, password);
//    }
//
    public void deleteMapPoint(MapPoints mapPoints) {
        mapPointDao.delete(mapPoints);

    }
//    public void register(Users user) {
//        userDao.insert(user);
//    }

    public void add(MapPoints mapPoint) {
        mapPointDao.insert(mapPoint);
    }
    public List<MapPoints> getAllMapPoints() {
        return mapPointDao.getAllMapPoints();
    }
}
