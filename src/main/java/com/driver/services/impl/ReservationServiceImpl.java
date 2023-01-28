package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        User user = userRepository3.findById(userId).get();
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        if(user == null || parkingLot == null){
            return null;
        }

        Reservation reservation = new Reservation();

        List<Spot> spotList = parkingLot.getSpotList();

        Collections.sort(spotList, (s1, s2) -> s1.getPricePerHour() - s2.getPricePerHour());

        boolean reserveSpot = false;

        for(Spot spot : spotList){

            int wheels = 0;
            if(spot.getSpotType() == SpotType.TWO_WHEELER){
                wheels = 2;
            }else if(spot.getSpotType() == SpotType.FOUR_WHEELER){
                wheels = 4;
            }else {
                wheels = Integer.MAX_VALUE;
            }

            if(numberOfWheels <= wheels && !spot.getOccupied()){
                reserveSpot = true;

                reservation.setSpot(spot);
                reservation.setUser(user);
                reservation.setNumberOfHours(timeInHours);

                spot.setOccupied(true);
                spot.getReservationList().add(reservation);

                user.getReservationList().add(reservation);

                userRepository3.save(user);
                spotRepository3.save(spot);
                reservationRepository3.save(reservation);

                break;
            }
        }

        if(reserveSpot == false){
            throw new Exception("Reservation cannot be made!!!");
        }

        return reservation;
    }
}
