package com.wqb.model;

import java.io.Serializable;
import java.util.List;

public class TBasicSubjectMessageForm implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2868042316812918136L;

    private TBasicSubjectMessage tBasicSubjectMessage;

    private List<TBasicSubjectMessage> tBasicSubjectMessageList;

    public TBasicSubjectMessage gettBasicSubjectMessage() {
        return tBasicSubjectMessage;
    }

    public void settBasicSubjectMessage(TBasicSubjectMessage tBasicSubjectMessage) {
        this.tBasicSubjectMessage = tBasicSubjectMessage;
    }

    public List<TBasicSubjectMessage> gettBasicSubjectMessageList() {
        return tBasicSubjectMessageList;
    }

    public void settBasicSubjectMessageList(List<TBasicSubjectMessage> tBasicSubjectMessageList) {
        this.tBasicSubjectMessageList = tBasicSubjectMessageList;
    }

}
