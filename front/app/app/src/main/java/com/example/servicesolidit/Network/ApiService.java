package com.example.servicesolidit.Network;



import com.example.servicesolidit.Utils.Dtos.Requests.CancelAppointmentRequestDto;
import com.example.servicesolidit.Utils.Dtos.Requests.CreateAgreementRequest;
import com.example.servicesolidit.Utils.Dtos.Requests.CreateAppointmentRequestDto;
import com.example.servicesolidit.Utils.Dtos.Requests.CreateCommentRequest;
import com.example.servicesolidit.Utils.Dtos.Requests.LoginRequestDto;
import com.example.servicesolidit.Utils.Dtos.Requests.RegisterRequestDto;
import com.example.servicesolidit.Utils.Dtos.Requests.ResetPasswordRequest;
import com.example.servicesolidit.Utils.Dtos.Requests.SendMessageRequest;
import com.example.servicesolidit.Utils.Dtos.Requests.UpdateToProviderRequestDto;
import com.example.servicesolidit.Utils.Dtos.Requests.UploadRelationalImageDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Agreements.AgreementResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentItemResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentListResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Appointment.AppointmentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Comments.EnableToCommentResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Comments.CommentsResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Conversatoins.ConversationResponse;
import com.example.servicesolidit.Utils.Dtos.Responses.Feed.FeedResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.GenerarMessageResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.ProviderImageLoadedResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.UploadImageResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.LoginResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Messages.SendMessageResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.RegisterResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.ProviderProfileInformationDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProfileResponseDto;
import com.example.servicesolidit.Utils.Dtos.Responses.User.UserInfoProviderProfileResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @POST("provider/imagen/{providerId}")
    Call<ProviderImageLoadedResponseDto> loadImageRelationalInformationProvider(@Path("providerId") int providerId, @Body UploadRelationalImageDto request);

    @POST("users/imagen/{userid}")
    Call<GenerarMessageResponseDto> loadImageRelationalInformationUserProfile(@Path("userid") int userid, @Body UploadRelationalImageDto request);

    @POST("appointment/cita/{id_provider}/{id_customer}")
    Call<AppointmentResponseDto> createAppointmnt(@Body CreateAppointmentRequestDto request, @Path("id_provider")int idProvider, @Path("id_customer") int idCustomer);

    @PUT("appointment/cancelar/{id_provider}")
    Call<AppointmentResponseDto> cancelAppointment(
            @Body CancelAppointmentRequestDto request,
            @Path("id_provider")int idProvider);

    // If response changes it needs to modify resposne object
    @POST("agrements/agendar/{idAppointment}/{idProvider}")
    Call<AppointmentResponseDto> createAgreement(@Body CreateAgreementRequest request, @Path("idAppointment") int idAppointment, @Path("idProvider") int idProvider);

    @GET("agrements/citas/{idProvider}")
    Call<AgreementResponseDto> obtainAgreements(@Path("idProvider")int idProvider);

    @GET("appointment/consulta/{idProvider}")
    Call<AppointmentListResponse> obtenerAppointmntsListAsProvider(@Path("idProvider") int idProvider);

    @GET("appointment/porUser/{idUser}")
    Call<ArrayList<AppointmentItemResponse>> obtenerAppointmntsListAsCustomer(@Path("idUser") int idUser);

    @GET("provider/provedores/{item}")
    Call<SearchProviderResponseDto> searchProvider(@Path("item")String item);

    @Multipart
    @POST("images/upload")
    Call<UploadImageResponseDto> uploadImage(@Part MultipartBody.Part image);

    @GET("message/mensajes/{idOrigen}/{idDestino}")
    Call<MessagesResponseDto> getMessages(@Path("idOrigen") int idOrigen, @Path("idDestino") int idDestino);

    @GET("message/conversations/{idLogged}")
    Call<ConversationResponse> getConversations(@Path("idLogged")int idLogged);

    @POST("message/mensajes/{idOrigen}/{idDestino}")
    Call<SendMessageResponseDto> sendMessages(@Path("idOrigen") int idOrigen, @Path("idDestino") int idDestino, @Body SendMessageRequest requestDto);

    @GET("relational/print/{tableToRelation}/{idUsedOn}/{funcionality}")
    Call<RelationalImagesResponseDto> getRelationalImages(@Path("tableToRelation")String tableToRelation, @Path("idUsedOn")String idUsedOn, @Path("funcionality")String funcionality);

    @GET("comments-vews/enable_comment/{idUser}/{idProvider}")
    Call<EnableToCommentResponseDto> enableToComentSection(@Path("idUser")int idLogged, @Path("idProvider") int idProviderToLoad);

    @GET("comments-vews/obtain-comments/{idProvider}")
    Call<CommentsResponseDto> getCommentsByProvider(@Path("idProvider")int idProviderToLoad);

    @POST("comments-vews/comment_user/{idLogged}/{idProviderToLoad}")
    Call<AppointmentResponseDto> createComment(@Path("idLogged")int idLogged, @Path("idProviderToLoad") int idProviderToLoad, @Body CreateCommentRequest request);

    @PUT("users/resetPassword")
    Call<GenerarMessageResponseDto> resetPassword(@Body ResetPasswordRequest emailTxt);
}
