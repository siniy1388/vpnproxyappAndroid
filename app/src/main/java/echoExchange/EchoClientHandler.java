package echoExchange;


import android.os.Environment;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static List<String> listIpServ;
    private final String request;
    private ByteBuf response ;
    private String sresponse;

    private static  String fconfPath;
          //  =            getFilesDir().getPath();


    private static  String fconf ;//=fconfPath+File.separator+"ovpn"+File.separator+"client.ovpn" ;
    private static  String fcacrt ;//=fconfPath+File.separator+"ovpn"+File.separator+"ca.crt" ;
    private static  String fclcrt ;//=fconfPath+File.separator+"ovpn";
    private static  String fclkey;// = fconfPath+File.separator+"ovpn";
    private static String fname = "";


    EchoClientHandler(String req,String pathh) {
        request = req;
        //if (!pathh.isEmpty()){
            fconfPath = pathh;
            fconf = fconfPath+File.separator+"ovpn"+File.separator+"client.ovpn" ;
            fcacrt = fconfPath+File.separator+"ovpn"+File.separator+"ca.crt" ;
            fclcrt =fconfPath+File.separator+"ovpn";
            fclkey = fconfPath+File.separator+"ovpn";
       // }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.copiedBuffer(request, CharsetUtil.UTF_8));

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
//        response = in;
//        sresponse = in.toString(CharsetUtil.UTF_8);
//
//
//        ctx.close();
//        String tstr = request.substring(0,4);
//        if ("file".equals(tstr)){
//           saveToFile(response);
//        }
        ctx.flush();
        String tstr = request.substring(0,4);
        if ("file".equals(tstr)){
            saveToFile(in);
        }else{
            response = in;
            sresponse = in.toString(CharsetUtil.UTF_8);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    public ByteBuf getResponse(){
        return response;
    }

    public String getResponses(){
        return sresponse;
    }

    public void saveToFile(ByteBuf fil) {
        ByteBuf tmpb = fil;
        String ffile;
        String id = request.substring(4,request.indexOf(":"));
        switch (id){
            case("1"):
                ffile = fconf;
                // Нужно получить имя клиентского файла из
                // файла конфигурации
                getFNameFromBBuf(tmpb);
                break;
            case("2"):
                ffile = fcacrt;
                break;
            case("3"):
                ffile = fclcrt +File.separator+getFname()+".crt";
                break;
            case("4"):
                ffile = fclkey +File.separator+getFname()+".key";
                break;
            default:
                ffile = fconf;
                break;

        }


        File file ;
        FileOutputStream fileoutputStream = null;
        FileChannel fileChannel = null;
        ByteBuffer byteBuffer = null;
        try {
            file = new File(ffile);
            fileoutputStream = new FileOutputStream(file,true);
            fileChannel = fileoutputStream.getChannel();
            //fileChannel.write(fil.nioBuffers());

            int length = fil.readableBytes();
            int rc = fil.refCnt();
            int written = 0;
            if (fil.nioBufferCount() == 1) {
                try{
                    byteBuffer = fil.nioBuffer();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                while (written < length) {
                    written += fileChannel.write(byteBuffer);
                }
            } else {
                ByteBuffer[] byteBuffers = fil.nioBuffers();
                while (written < length) {
                    written += fileChannel.write(byteBuffers);
                }
            }

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }finally{
            try {
                fileChannel.force(false);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fileoutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fileChannel.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getFNameFromBBuf(ByteBuf bb){
        String tstr;
        tstr = new String(ByteBufUtil.getBytes(bb), Charset.forName("UTF-8"));
        String ret = "";
//        byte[] t = null;
//        try{
//            t = bb.array();
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
        // String tstr = new String(t);
        int ind =tstr.indexOf("ca.crt",30);
        for (int i = ind+25; i < ind+30; i++) {
            if (tstr.charAt(i)!='.'){
                ret = ret+tstr.charAt(i);
            }else{
                setFname("client"+ret);
                break;
            }
        }
    }

    private void setFname(String fn){
        fname = fn;
    }

    private String getFname(){
        return fname;
    }
}