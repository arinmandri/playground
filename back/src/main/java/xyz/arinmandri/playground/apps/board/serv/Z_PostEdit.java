package xyz.arinmandri.playground.apps.board.serv;

import java.util.List;


public record Z_PostEdit (
        String content ,
        List<Z_PAttachmentNoo> attachments )
{}
