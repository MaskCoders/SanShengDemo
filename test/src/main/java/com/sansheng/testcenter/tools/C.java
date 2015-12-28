package com.sansheng.testcenter.tools;

/**
    控制域
 d7        d6         d5                d4             d3 d2 d1 d0
 传输方向位  启动标志位   帧计数位FCB     帧计数有效位FCV       功能码
 DIR         PRM      要求访问位ACD     保留
 0ser->cli   1formzhu  注释1           1FCB位有效
 1cli->ser   0fromcong                 0 FCB位无效
 注释1：
 当帧计数有效位FCV=1时，FCB表示每个站连续的发送/确认或者请求/响应服务的变化位。FCB位用来防止信息传输的丢失和重复。

 启动站向同一从动站传输新的发送/确认或请求/响应传输服务时，将FCB取相反值。启动站保存每一个从动站FCB值，若超时未收到从动站的报文，或接收出现差错，则启动站不改变FCB的状态，重复原来的发送/确认或者请求/响应服务。

 复位命令中的FCB=0，从动站接收复位命令后将FCB置“0”。

 ACD位用于上行响应报文中。ACD=1表示终端有重要事件等待访问，则附加信息域中带有事件计数器EC（EC见本部分4.3.4.6.3）；ACD=0表示终端无事件数据等待访问。

 ACD置“1”和置“0”规则：

 ——自上次收到报文后发生新的重要事件，ACD位置“1”；

 ——收到主站请求事件报文并执行后，ACD位置“0”。

 */
public class C {
    private int d7;
    private int d6;
    private int d5;
    private int d4;
    private int d3210;
    public void parse(byte data){

    }
    public C(){

    }
    public C(boolean d7,boolean d6,boolean d5,boolean d4,int d3210){
        this.d7 = d7?128:0;
        this.d6 = d6?64:0;
        this.d5 = d5?32:0;
        this.d4 = d4?16:0;
        this.d3210 = d3210;
    }
    public String getCommand(){
        return Integer.toHexString(d7+d6+d5+d4+d3210).toUpperCase();
    }
}
