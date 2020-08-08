import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

public class EchoServer {

    public static void main(String[] args) {
        // accept线程组，用来接受连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // I/O线程组， 用于处理业务逻辑
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try {
            // 服务端启动引导
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup) // 绑定两个线程组
                    .channel(NioServerSocketChannel.class) // 指定通道类型
                    .option(ChannelOption.SO_BACKLOG, 100) // 设置TCP连接的缓冲区
                    .handler(new LoggingHandler(LogLevel.INFO)) // 设置日志级别
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline(); // 获取处理器链
                                    pipeline.addLast(new EchoServerHandler()); // 添加新的件处理器
                                }
                            });

            // 通过bind启动服务
            ChannelFuture f = b.bind(8080).sync();
            // 阻塞主线程，知道网络服务被关闭
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 每当从客户端收到新的数据时，这个方法会在收到消息时被调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到数据：" + ((ByteBuf) msg).toString(Charset.defaultCharset()));
        ctx.write(Unpooled.wrappedBuffer("Server message".getBytes()));
        ctx.fireChannelRead(msg);
    }

    // 数据读取完后被调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    // 当Netty由于IO错误或者处理器在处理事件时抛出的异常时被调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}