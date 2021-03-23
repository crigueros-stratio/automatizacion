package com.stratio.comfandi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stratio.comfandi.model.ParametrizacionIngesta;
import com.stratio.comfandi.repository.ParametrizacionRepository;

@Service
public class ParametrizacionService {

	@Autowired
	private ParametrizacionRepository parametrizacionRepository;

	public List<ParametrizacionIngesta> findAll() {
		return parametrizacionRepository.findAll();
	}

	public List<ParametrizacionIngesta> findEstado(Integer estado) {
		return parametrizacionRepository.findByEstado(estado);
	}
	
	public List<ParametrizacionIngesta> findWithCondicionFechas(Integer estado) {
		return parametrizacionRepository.findWithCondicion(estado);
	}
}
