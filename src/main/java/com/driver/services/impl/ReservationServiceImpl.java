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

            int minPrice = Integer.MAX_VALUE;
            reserveSpot = false;
            Spot bookedSpot = null;

            if(requestedSpot.equals(SpotType.OTHERS)){
                for(Spot s : spotList) {
                    if(s.getSpotType().equals(SpotType.OTHERS)){
                        int price = s.getPricePerHour() * timeInHours;
                        if(!s.getOccupied() && price < minPrice){
                            minPrice = price;
                            reserveSpot = true;
                            bookedSpot = s;
                        }
                    }
                }
            }else if(requestedSpot.equals(SpotType.FOUR_WHEELER)){
                for(Spot s : spotList) {
                    if(s.getSpotType().equals(SpotType.OTHERS) || s.getSpotType().equals(SpotType.FOUR_WHEELER)){
                        int price = s.getPricePerHour() * timeInHours;
                        if(!s.getOccupied() && price < minPrice){
                            minPrice = price;
                            reserveSpot = true;
                            bookedSpot = s;
                        }
                    }
                }
            }else if(requestedSpot.equals(SpotType.TWO_WHEELER)){
                for(Spot s : spotList) {
                    int price = s.getPricePerHour() * timeInHours;
                    if(!s.getOccupied() && price < minPrice){
                        minPrice = price;
                        reserveSpot = true;
                        bookedSpot = s;
                    }
                }
            }

            if(reserveSpot == false){
                throw new Exception("Cannot make reservation");
            }

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(bookedSpot);
            reservation.setUser(user);

            bookedSpot.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(bookedSpot);

            return reservation;

        }catch(Exception e){
            return  null;
        }
    }
}
