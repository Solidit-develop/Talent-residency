package com.example.servicesolidit.Network;



import com.example.servicesolidit.Utils.Models.Requests.CancelAppointmentRequestDto;
import com.example.servicesolidit.Utils.Models.Requests.CreateAgreementRequest;
import com.example.servicesolidit.Utils.Models.Requests.CreateAppointmentRequestDto;
import com.example.servicesolidit.Utils.Models.Requests.LoginRequestDto;
import com.example.servicesolidit.Utils.Models.Requests.RegisterRequestDto;
import com.example.servicesolidit.Utils.Models.Requests.SendMessageRequest;
import com.example.servicesolidit.Utils.Models.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Models.Responses.Appointment.AppointmentResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Conversatoins.ConversationResponse;
import com.example.servicesolidit.Utils.Models.Responses.Feed.FeedResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.LoginResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.Messages.SendMessageResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.RegisterResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.SearchProvider.SearchProviderResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.ProviderProfileInformationDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProfileResponseDto;
import com.example.servicesolidit.Utils.Models.Responses.User.UserInfoProviderProfileResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto request);

    @POST("users/register")
    Call<RegisterResponseDto> register(@Body RegisterRequestDto request);

    @GET("users/information/{idToFind}")
    Call<UserInfoProfileResponseDto> information(@Path("idToFind")int idUserToFind);

    @POST("provider/correo")
    Call<RegisterResponseDto> updateToUserProvider(@Body UpdateToProviderRequestDto request);

    @GET("provider/user/{idToFind}")
    Call<UserInfoProviderProfileResponse> informationProviderByUserId(@Path("idToFind")int idUserToFind);

    @GET("provider/provider/{idToFind}")
    Call<ProviderProfileInformationDto> informationProviderByProviderId(@Path("idToFind")int idUserToFind);

    @GET("provider/todos/services/feed")
    Call<FeedResponseDto> feed();

    @POST("appointment/cita/{id_provider}/{id_customer}")
    Call<AppointmentResponseDto> createAppointmnt(@Body CreateAppointmentRequestDto request, @Path("id_provider")int idProvider, @Path("id_customer") int idCustomer);

    @PUT("appointment/cancelar/{id_provider}")
    Call<AppointmentResponseDto> cancelAppointment(
            @Body CancelAppointmentRequestDto request,
            @Path("id_provider")int idProvider);

    // If response changes it needs to modify resposne object
    @POST("agrements/agendar/{idAppointment}/{idProvider}")
    Call<AppointmentResponseDto> createAgreement(@Body CreateAgreementRequest request, @Path("idAppointment") int idAppointment, @Path("idProvider") int idProvider);

    @GET("appointment/consulta/{idProvider}")
    Call<AppointmentListResponse> obtenerAppointmntsListAsProvider(@Path("idProvider") int idProvider);

    @GET("appointment/porUser/{idUser}")
    Call<ArrayList<AppointmentItemResponse>> obtenerAppointmntsListAsCustomer(@Path("idUser") int idUser);

    @GET("provider/provedores/{item}")
    Call<List<SearchProviderResponseDto>> searchProvider(@Path("item")String item);

    @GET("message/mensajes/{idOrigen}/{idDestino}")
    Call<MessagesResponseDto> getMessages(@Path("idOrigen") int idOrigen, @Path("idDestino") int idDestino);

    @GET("message/conversations/{idLogged}")
    Call<ConversationResponse> getConversations(@Path("idLogged")int idLogged);

    @POST("message/mensajes/{idOrigen}/{idDestino}")
    Call<SendMessageResponseDto> sendMessages(@Path("idOrigen") int idOrigen, @Path("idDestino") int idDestino, @Body SendMessageRequest requestDto);

    @GET("relational/print/{tableToRelation}/{idUsedOn}/{funcionality}")
    Call<RelationalImagesResponseDto> getRelationalImages(@Path("tableToRelation")String tableToRelation, @Path("idUsedOn")String idUsedOn, @Path("funcionality")String funcionality);
}
