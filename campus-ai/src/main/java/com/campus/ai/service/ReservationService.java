package com.campus.ai.service;

import com.campus.ai.entity.Reservation;

public interface ReservationService {

    void insert(Reservation reservation);

    Reservation findByPhone(String phone);
}
