package cdc.cdcpostgres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cdc")
@CrossOrigin(origins = "*") // Enable CORS for Angular
public class CdcEventController {

    @Autowired
    private CdcEventRepository repository;

//  @GetMapping
//    public List<CdcEvent> getAllEvents() {
//        return repository.findAll(Sort.by(Sort.Direction.DESC, "eventTime"));
//    }

    @GetMapping("/cdc-events")
    public List<CdcEvent> getAllEvents() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "eventTime"));
    }


}

