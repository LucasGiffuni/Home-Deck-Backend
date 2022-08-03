package com.home.app.integrations;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2.AgentName;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.Intent;
import com.google.cloud.dialogflow.v2.IntentsClient;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryParameters;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SentimentAnalysisRequestConfig;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.Intent.Message;
import com.google.cloud.dialogflow.v2.Intent.TrainingPhrase;
import com.google.cloud.dialogflow.v2.Intent.Message.Text;
import com.google.cloud.dialogflow.v2.Intent.TrainingPhrase.Part;
import com.google.common.collect.Maps;
import com.home.app.model.response.DialogFlowIntentResponse;
import com.home.app.model.response.DialogFlowIntentResponseData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DialogFlow {

  public static DialogFlowIntentResponse detectIntentSentimentAnalysis(
      String projectId, String text, String sessionId, String languageCode)
      throws IOException, ApiException {
    QueryResult queryResult;
    DialogFlowIntentResponse df = new DialogFlowIntentResponse();

    Map<String, QueryResult> queryResults = Maps.newHashMap();
    // Instantiates a client
    try (SessionsClient sessionsClient = SessionsClient.create()) {
      // Set the session name using the sessionId (UUID) and projectID (my-project-id)
      SessionName session = SessionName.of(projectId, sessionId);
      System.out.println("Session Path: " + session.toString());

      // Detect intents for each text input

      // Set the text (hello) and language code (en-US) for the query
      TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);

      // Build the query with the TextInput
      QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

      //
      SentimentAnalysisRequestConfig sentimentAnalysisRequestConfig = SentimentAnalysisRequestConfig.newBuilder()
          .setAnalyzeQueryTextSentiment(true).build();

      QueryParameters queryParameters = QueryParameters.newBuilder()
          .setSentimentAnalysisRequestConfig(sentimentAnalysisRequestConfig)
          .build();
      DetectIntentRequest detectIntentRequest = DetectIntentRequest.newBuilder()
          .setSession(session.toString())
          .setQueryInput(queryInput)
          .setQueryParams(queryParameters)
          .build();

      // Performs the detect intent request
      DetectIntentResponse response = sessionsClient.detectIntent(detectIntentRequest);

      // Display the query result
      queryResult = response.getQueryResult();

      System.out.println("====================");
      System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
      System.out.format(
          "Detected Intent: %s (confidence: %f)\n",
          queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());

      System.out.format(
          "Fulfillment Text: '%s'\n",
          queryResult.getFulfillmentMessagesCount() > 0
              ? queryResult.getFulfillmentMessages(0).getText()
              : "Triggered Default Fallback Intent");
      System.out.format(
          "Sentiment Score: '%s'\n",
          queryResult.getSentimentAnalysisResult().getQueryTextSentiment().getScore());
      queryResults.put(text, queryResult);
      df.setDiaglogFlowResponse(text, queryResult.getIntent().getDisplayName(),
          queryResult.getFulfillmentMessages(0).getText().getText(0),
          queryResult.getSentimentAnalysisResult().getQueryTextSentiment().getScore());

    }

    return df;
  }

  public static Intent createIntent(
      String displayName,
      String projectId,
      List<String> trainingPhrasesParts,
      List<String> messageTexts)
      throws ApiException, IOException {
    // Instantiates a client
    try (IntentsClient intentsClient = IntentsClient.create()) {
      // Set the project agent name using the projectID (my-project-id)
      AgentName parent = AgentName.of(projectId);

      // Build the trainingPhrases from the trainingPhrasesParts
      List<TrainingPhrase> trainingPhrases = new ArrayList<>();
      for (String trainingPhrase : trainingPhrasesParts) {
        trainingPhrases.add(
            TrainingPhrase.newBuilder()
                .addParts(Part.newBuilder().setText(trainingPhrase).build())
                .build());
      }

      // Build the message texts for the agent's response
      Message message = Message.newBuilder().setText(Text.newBuilder().addAllText(messageTexts).build()).build();

      // Build the intent
      Intent intent = Intent.newBuilder()
          .setDisplayName(displayName)
          .addMessages(message)
          .addAllTrainingPhrases(trainingPhrases)
          .build();

      // Performs the create intent request
      Intent response = intentsClient.createIntent(parent, intent);
      System.out.format("Intent created: %s\n", response);

      return response;
    }
  }

}