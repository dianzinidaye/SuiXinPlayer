package com.example.suixinplayer.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class SongBean extends LitePalSupport implements Serializable {

    /**
     * status : 1
     * err_code : 0
     * data : {"hash":"67f4b520ee80d68959f4bf8a213f6774","timelength":166006,"filesize":2657456,"audio_name":"毛不易 - 像我这样的人 (Live)","have_album":1,"album_name":"\u201c用奋斗点亮幸福\u201d江苏卫视2019跨年演唱会","album_id":"14275743","img":"http://imge.kugou.com/stdmusic/20181231/20181231214942859068.jpg","have_mv":0,"video_id":0,"author_name":"毛不易","song_name":"像我这样的人 (Live)","lyrics":"﻿[id:$00000000]\r\n[ar:毛不易]\r\n[ti:像我这样的人(Live)]\r\n[by:]\r\n[hash:67f4b520ee80d68959f4bf8a213f6774]\r\n[al:]\r\n[sign:]\r\n[qq:]\r\n[total:166006]\r\n[offset:0]\r\n[00:00.42]毛不易 - 像我这样的人(Live)\r\n[00:02.77]作词：毛不易\r\n[00:04.05]作曲：毛不易\r\n[00:15.46]像我这样优秀的人\r\n[00:19.50]本该灿烂过一生\r\n[00:23.14]怎么二十多年到头来\r\n[00:27.12]还在人海里浮沉\r\n[00:31.01]像我这样聪明的人\r\n[00:34.85]早就告别了单纯\r\n[00:38.63]怎么还是用了一段情\r\n[00:42.57]去换一身伤痕\r\n[00:46.51]像我这样迷茫的人\r\n[00:50.35]像我这样寻找的人\r\n[00:54.29]像我这样碌碌无为的人\r\n[00:58.17]你还见过多少人\r\n[01:21.42]像我这样庸俗的人\r\n[01:25.22]从不喜欢装深沉\r\n[01:28.95]怎么偶尔听到老歌时\r\n[01:32.87]忽然也晃了神\r\n[01:36.77]像我这样懦弱的人\r\n[01:40.66]凡事都要留几分\r\n[01:44.58]怎么曾经也会为了谁\r\n[01:48.44]想过奋不顾身\r\n[01:52.27]像我这样迷茫的人\r\n[01:56.16]像我这样寻找的人\r\n[01:59.69]像我这样碌碌无为的人\r\n[02:04.03]你还见过多少人\r\n[02:07.87]像我这样孤单的人\r\n[02:11.60]像我这样傻的人\r\n[02:15.59]像我这样不甘平凡的人\r\n[02:19.33]世界上有多少人\r\n[02:25.18]像我这样莫名其妙的人\r\n[02:29.56]会不会有人心疼\r\n","author_id":"722869","privilege":8,"privilege2":"1000","play_url":"https://webfs.yun.kugou.com/201911130145/cac9623f51d4562688a0edde370c2e5b/G146/M07/02/14/cpQEAFwqEt2AMf4dACiMsITsnwk265.mp3","authors":[{"author_id":"722869","is_publish":"1","sizable_avatar":"http://singerimg.kugou.com/uploadpic/softhead/{size}/20190802/20190802101420425.jpg","author_name":"毛不易","avatar":"http://singerimg.kugou.com/uploadpic/softhead/400/20190802/20190802101420425.jpg"}],"is_free_part":0,"bitrate":128,"audio_id":"48908153","play_backup_url":"https://webfs.cloud.kugou.com/201911130145/0da05b57e781d555274a7f9431d454fb/G146/M07/02/14/cpQEAFwqEt2AMf4dACiMsITsnwk265.mp3"}
     */

    private int status;
    private int err_code;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * hash : 67f4b520ee80d68959f4bf8a213f6774
         * timelength : 166006
         * filesize : 2657456
         * audio_name : 毛不易 - 像我这样的人 (Live)
         * have_album : 1
         * album_name : “用奋斗点亮幸福”江苏卫视2019跨年演唱会
         * album_id : 14275743
         * img : http://imge.kugou.com/stdmusic/20181231/20181231214942859068.jpg
         * have_mv : 0
         * video_id : 0
         * author_name : 毛不易
         * song_name : 像我这样的人 (Live)
         * lyrics : ﻿[id:$00000000]
         [ar:毛不易]
         [ti:像我这样的人(Live)]
         [by:]
         [hash:67f4b520ee80d68959f4bf8a213f6774]
         [al:]
         [sign:]
         [qq:]
         [total:166006]
         [offset:0]
         [00:00.42]毛不易 - 像我这样的人(Live)
         [00:02.77]作词：毛不易
         [00:04.05]作曲：毛不易
         [00:15.46]像我这样优秀的人
         [00:19.50]本该灿烂过一生
         [00:23.14]怎么二十多年到头来
         [00:27.12]还在人海里浮沉
         [00:31.01]像我这样聪明的人
         [00:34.85]早就告别了单纯
         [00:38.63]怎么还是用了一段情
         [00:42.57]去换一身伤痕
         [00:46.51]像我这样迷茫的人
         [00:50.35]像我这样寻找的人
         [00:54.29]像我这样碌碌无为的人
         [00:58.17]你还见过多少人
         [01:21.42]像我这样庸俗的人
         [01:25.22]从不喜欢装深沉
         [01:28.95]怎么偶尔听到老歌时
         [01:32.87]忽然也晃了神
         [01:36.77]像我这样懦弱的人
         [01:40.66]凡事都要留几分
         [01:44.58]怎么曾经也会为了谁
         [01:48.44]想过奋不顾身
         [01:52.27]像我这样迷茫的人
         [01:56.16]像我这样寻找的人
         [01:59.69]像我这样碌碌无为的人
         [02:04.03]你还见过多少人
         [02:07.87]像我这样孤单的人
         [02:11.60]像我这样傻的人
         [02:15.59]像我这样不甘平凡的人
         [02:19.33]世界上有多少人
         [02:25.18]像我这样莫名其妙的人
         [02:29.56]会不会有人心疼
         * author_id : 722869
         * privilege : 8
         * privilege2 : 1000
         * play_url : https://webfs.yun.kugou.com/201911130145/cac9623f51d4562688a0edde370c2e5b/G146/M07/02/14/cpQEAFwqEt2AMf4dACiMsITsnwk265.mp3
         * authors : [{"author_id":"722869","is_publish":"1","sizable_avatar":"http://singerimg.kugou.com/uploadpic/softhead/{size}/20190802/20190802101420425.jpg","author_name":"毛不易","avatar":"http://singerimg.kugou.com/uploadpic/softhead/400/20190802/20190802101420425.jpg"}]
         * is_free_part : 0
         * bitrate : 128
         * audio_id : 48908153
         * play_backup_url : https://webfs.cloud.kugou.com/201911130145/0da05b57e781d555274a7f9431d454fb/G146/M07/02/14/cpQEAFwqEt2AMf4dACiMsITsnwk265.mp3
         */

        private String hash;
        private int timelength;
        private int filesize;
        private String audio_name;
        private int have_album;
        private String album_name;
        private String album_id;
        private String img;
        private int have_mv;
        private int video_id;
        private String author_name;
        private String song_name;
        private String lyrics;
        private String author_id;
        private int privilege;
        private String privilege2;
        private String play_url;
        private int is_free_part;
        private int bitrate;
        private String audio_id;
        private String play_backup_url;
        private List<AuthorsBean> authors;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getTimelength() {
            return timelength;
        }

        public void setTimelength(int timelength) {
            this.timelength = timelength;
        }

        public int getFilesize() {
            return filesize;
        }

        public void setFilesize(int filesize) {
            this.filesize = filesize;
        }

        public String getAudio_name() {
            return audio_name;
        }

        public void setAudio_name(String audio_name) {
            this.audio_name = audio_name;
        }

        public int getHave_album() {
            return have_album;
        }

        public void setHave_album(int have_album) {
            this.have_album = have_album;
        }

        public String getAlbum_name() {
            return album_name;
        }

        public void setAlbum_name(String album_name) {
            this.album_name = album_name;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getHave_mv() {
            return have_mv;
        }

        public void setHave_mv(int have_mv) {
            this.have_mv = have_mv;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getSong_name() {
            return song_name;
        }

        public void setSong_name(String song_name) {
            this.song_name = song_name;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public int getPrivilege() {
            return privilege;
        }

        public void setPrivilege(int privilege) {
            this.privilege = privilege;
        }

        public String getPrivilege2() {
            return privilege2;
        }

        public void setPrivilege2(String privilege2) {
            this.privilege2 = privilege2;
        }

        public String getPlay_url() {
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }

        public int getIs_free_part() {
            return is_free_part;
        }

        public void setIs_free_part(int is_free_part) {
            this.is_free_part = is_free_part;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public String getAudio_id() {
            return audio_id;
        }

        public void setAudio_id(String audio_id) {
            this.audio_id = audio_id;
        }

        public String getPlay_backup_url() {
            return play_backup_url;
        }

        public void setPlay_backup_url(String play_backup_url) {
            this.play_backup_url = play_backup_url;
        }

        public List<AuthorsBean> getAuthors() {
            return authors;
        }

        public void setAuthors(List<AuthorsBean> authors) {
            this.authors = authors;
        }

        public static class AuthorsBean {
            /**
             * author_id : 722869
             * is_publish : 1
             * sizable_avatar : http://singerimg.kugou.com/uploadpic/softhead/{size}/20190802/20190802101420425.jpg
             * author_name : 毛不易
             * avatar : http://singerimg.kugou.com/uploadpic/softhead/400/20190802/20190802101420425.jpg
             */

            private String author_id;
            private String is_publish;
            private String sizable_avatar;
            private String author_name;
            private String avatar;

            public String getAuthor_id() {
                return author_id;
            }

            public void setAuthor_id(String author_id) {
                this.author_id = author_id;
            }

            public String getIs_publish() {
                return is_publish;
            }

            public void setIs_publish(String is_publish) {
                this.is_publish = is_publish;
            }

            public String getSizable_avatar() {
                return sizable_avatar;
            }

            public void setSizable_avatar(String sizable_avatar) {
                this.sizable_avatar = sizable_avatar;
            }

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }
        }
    }
}
