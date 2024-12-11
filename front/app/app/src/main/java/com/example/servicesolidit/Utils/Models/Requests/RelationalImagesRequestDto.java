package com.example.servicesolidit.Utils.Models.Requests;

import com.example.servicesolidit.Utils.Models.Responses.ImagesRelational.RelationalImagesResponseDto;

public class RelationalImagesRequestDto {
    private String  table;
    private String idUsedOn;
    private String funcionalida;

    public RelationalImagesRequestDto(){}

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getIdUsedOn() {
        return idUsedOn;
    }

    public void setIdUsedOn(String idUsedOn) {
        this.idUsedOn = idUsedOn;
    }

    public String getFuncionalida() {
        return funcionalida;
    }

    public void setFuncionalida(String funcionalida) {
        this.funcionalida = funcionalida;
    }
}
