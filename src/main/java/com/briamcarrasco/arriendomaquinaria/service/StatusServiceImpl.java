package com.briamcarrasco.arriendomaquinaria.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.briamcarrasco.arriendomaquinaria.repository.StatusRepository;
import com.briamcarrasco.arriendomaquinaria.model.Status;
import java.util.List;


@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<Status> findAll() {
        return statusRepository.findAll();
    }
    
}
