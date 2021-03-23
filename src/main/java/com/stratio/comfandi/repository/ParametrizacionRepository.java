package com.stratio.comfandi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stratio.comfandi.model.ParametrizacionIngesta;


public interface ParametrizacionRepository extends JpaRepository<ParametrizacionIngesta, Long> {
	
	
    List<ParametrizacionIngesta> findByEstado(Integer estado);
    
    
    @Query(value = "SELECT * FROM ingesta WHERE estado = :estado and (fecha_inicio_ingesta is null and fecha_fin_ingesta is null) "
    		+ "or (fecha_inicio_ingesta is not null and fecha_fin_ingesta is not null and (current_date - fecha_fin_ingesta) >= periodicidad) ", nativeQuery=true)
    List<ParametrizacionIngesta> findWithCondicion(Integer estado);
}
