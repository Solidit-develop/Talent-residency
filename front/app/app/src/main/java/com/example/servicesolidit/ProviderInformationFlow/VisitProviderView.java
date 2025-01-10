package com.example.servicesolidit.ProviderInformationFlow;

import com.example.servicesolidit.Utils.Dtos.Responses.Comments.CommentsDto;
import com.example.servicesolidit.Utils.Dtos.Responses.ImagesRelational.RelationalImagesResponseDto;

import java.util.ArrayList;

public interface VisitProviderView {
    void onSuccessGetImageInformation(RelationalImagesResponseDto response);
    void onErrorGetImageInformation(String error);
    void onShowProgress();
    void onHideProgress();
    void onErrorEnableCommentsSection(String error);
    void enableCommentsSection(boolean enableToComment);

    void onSuccessObtainComments(ArrayList<CommentsDto> response);

    void onErrorObtainComments(String s);

    void onCommentCreatedSuccess(String message);

    void onCommentCreatedError(String ocurriUnError);
}
