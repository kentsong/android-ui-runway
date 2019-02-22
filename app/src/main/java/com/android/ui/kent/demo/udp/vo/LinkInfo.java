package com.android.ui.kent.demo.udp.vo;

/**
 * Created by Kent Song on 2019/2/20.
 * 万兆网路信息
 */
public class LinkInfo {

    private String receiveDbm = ""; //接收功率
    private String linkStatus = ""; //连接状态

    private LinkInfo(Builder builder) {
        receiveDbm = builder.receiveDbm;
        linkStatus = builder.linkStatus;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getReceiveDbm() {
        return receiveDbm;
    }

    public String getLinkStatus() {
        return linkStatus;
    }


    public static final class Builder {
        private String receiveDbm;
        private String linkStatus;

        private Builder() {
        }

        public Builder setReceiveDbm(String val) {
            receiveDbm = val;
            return this;
        }

        public Builder setLinkStatus(String val) {
            linkStatus = val;
            return this;
        }

        public LinkInfo build() {
            return new LinkInfo(this);
        }
    }
}
