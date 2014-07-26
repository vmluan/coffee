package com.luanvm.coffee.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.luanvm.coffee.domain.TH_Encounter;


public interface EncounterRepository extends PagingAndSortingRepository<TH_Encounter, Integer> {
	

}
