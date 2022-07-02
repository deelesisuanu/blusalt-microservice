package com.blusalt.dele.consumer_service.services;

import com.blusalt.dele.consumer_service.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BaseService {

    private final BaseRepository baseRepository;

}
