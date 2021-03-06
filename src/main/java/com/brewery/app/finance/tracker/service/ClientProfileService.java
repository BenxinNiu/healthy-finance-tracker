package com.brewery.app.finance.tracker.service;

import com.brewery.app.finance.tracker.codegen.model.ClientAccountUpdate;
import com.brewery.app.finance.tracker.codegen.model.ClientCreationPostRequest;
import com.brewery.app.finance.tracker.codegen.model.ClientProfile;
import com.brewery.app.finance.tracker.model.ClientCreditAccountModel;
import com.brewery.app.finance.tracker.model.ClientProfileModel;
import com.brewery.app.finance.tracker.repository.ClientProfileModelRepository;
import com.brewery.app.finance.tracker.transformer.ClientCreditAccountTransformer;
import com.brewery.app.finance.tracker.transformer.ClientProfileTransformer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ClientProfileService {

    private final ClientProfileModelRepository clientProfileModelRepository;
    private final ClientProfileTransformer clientProfileTransformer;
    private final ClientCreditAccountTransformer clientCreditAccountTransformer;
    private final BalanceCalculationService balanceCalculationService;

    public Mono<ClientProfile> createNewClientProfile(Mono<ClientCreationPostRequest> clientCreationPostRequest) {
        return clientCreationPostRequest
                .map(clientProfileTransformer::newProfileModel)
                .flatMap(clientProfileModelRepository::save)
                .map(clientProfileTransformer::toDto);
    }

    public Mono<ClientProfile> getClientProfileById(String id) {
        return clientProfileModelRepository.getClientProfileModelById(id)
                .map(clientProfileTransformer::toDto);
    }

    public Mono<ClientProfileModel> getClientProfileModelById(String id) {
        return clientProfileModelRepository.getClientProfileModelById(id);
    }

    public Mono<ClientProfile> patchClientProfile(String id, List<ClientAccountUpdate> accountUpdates) {
        Mono<ClientProfileModel> updatedProfile = clientProfileModelRepository.getClientProfileModelById(id)
                .map(profile -> {
                    this.updateClientCreditAccountList(profile, accountUpdates);
                    profile.setLastUpdated(LocalDateTime.now());
                    return balanceCalculationService.updateClientProfileBalanceHistory(profile);
                });
        return updatedProfile
                .flatMap(clientProfileModelRepository::save)
                .map(clientProfileTransformer::toDto);
    }

    public Mono<ClientProfile> saveClientProfile(ClientProfileModel clientProfileModel) {
        return clientProfileModelRepository.save(clientProfileModel)
                .map(clientProfileTransformer::toDto);
    }

    private void updateClientCreditAccountList(ClientProfileModel profile, List<ClientAccountUpdate> update) {
        // Add new accounts
        update.stream()
                .filter(accountUpdate -> accountUpdate.getAction() == ClientAccountUpdate.ActionEnum.ADD)
                .forEach(accountUpdate -> {
                    ClientCreditAccountModel newModel = clientCreditAccountTransformer.toNewModel(accountUpdate);
                    profile.getClientCreditAccountModelList().add(newModel);
                });

        // Remove existing
        List<String> toRemoveAccountIds = update.stream()
                .filter(accountUpdate -> accountUpdate.getAction() == ClientAccountUpdate.ActionEnum.REMOVE)
                .map(ClientAccountUpdate::getId)
                .collect(Collectors.toList());

        profile.setClientCreditAccountModelList(
                profile.getClientCreditAccountModelList().stream()
                        .filter(model -> !toRemoveAccountIds.contains(model.getAccountId()))
                        .collect(Collectors.toList())
        );
    }
}
