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

        try{
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            if(user == null || parkingLot == null){
                throw new Exception("Cannot make reservation");
            }

            List<Spot> spotList = parkingLot.getSpotList();
            boolean reserveSpot = false;

            for(Spot s : spotList){
                if(s.getOccupied() == false){
                    reserveSpot = true;
                    break;
                }
            }

            if(reserveSpot == false){
                throw new Exception("Cannot make reservation");
            }

            SpotType requestedSpot;

            if(numberOfWheels <= 2){
                requestedSpot = SpotType.TWO_WHEELER;
            }else if(numberOfWheels <= 4){
                requestedSpot = SpotType.FOUR_WHEELER;
            }else{
                requestedSpot = SpotType.OTHERS;
            }

            int minPrice;

            for(Spot s : spotList){
                if(requestedSpot.equals(s.getSpotType())){

                }
            }




        }catch(Exception e){
            throw new Exception("Cannot make reservation");
        }

        Reservation reservation = new Reservation();

        return reservation;
    }
}
