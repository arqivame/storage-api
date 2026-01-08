package com.arqivame.storage.infrastructure.configuration.messaging;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConsumerConfig {

//     @Bean
//     Consumer<Message<FileUploadSessionAbortedMessage>> fileUploadSessionCanceledConsumer(
//             @Value("${application.messaging.consumer.file-upload-session-canceled-event.max-attempts}") final Long maxAttempts,
//             @Qualifier("fileUploadSessionCanceledEventError") final MessageProducer<FileUploadSessionAbortedMessage> errorMessageProducer,
//             final MarkUploadSessionForDeletionUseCase markUploadSessionForDeletionUseCase) {
//         return new FileUploadSessionCanceledConsumer(
//                 maxAttempts,
//                 errorMessageProducer,
//                 markUploadSessionForDeletionUseCase);
//     }

//     @Bean
//     Consumer<Message<FileUploadSessionMarkedForDeletionMessage>> fileUploadSessionMarkedForDeletionConsumer(
//             @Value("${application.messaging.consumer.file-upload-session-marked-for-deletion-event.max-attempts}") final Long maxAttempts,
//             @Qualifier("fileUploadSessionMarkedForDeletionEventError") final MessageProducer<FileUploadSessionMarkedForDeletionMessage> errorMessageProducer,
//             final PhysicalUploadSessionDeleteUseCase physicalUploadSessionDeleteUseCase) {
//         return new FileUploadSessionMarkedForDeletionConsumer(
//                 maxAttempts,
//                 errorMessageProducer,
//                 physicalUploadSessionDeleteUseCase);
//     }

//     @Bean
//     Consumer<Message<FileUploadSessionProcessingInitiatedMessage>> fileUploadSessionProcessingInitiatedConsumer(
//             @Value("${application.messaging.consumer.file-upload-session-processing-initiated-event.max-attempts}") final Long maxAttempts,
//             @Qualifier("fileUploadSessionProcessingInitiatedEventError") final MessageProducer<FileUploadSessionProcessingInitiatedMessage> errorMessageProducer,
//             final AssembleFileUseCase mergeUploadSessionChunksUseCase) {
//         return new FileUploadSessionProcessingInitiatedConsumer(
//                 maxAttempts,
//                 errorMessageProducer,
//                 mergeUploadSessionChunksUseCase);
//     }

}
