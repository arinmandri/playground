package xyz.arinmandri.playground.serv.board;

import java.util.List;


public record Z_PostEdit (
        String content ,
        List<Z_PAttachment> attachments )
{
}
