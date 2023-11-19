package com.example.m_hike.Models;

public class Obs {
        private int obs_id;
        private int hike_id;
        private String obs_name;
        private String obs_time;
        private String obs_level;
        private String obs_distance;
        private byte[] obs_img;
        private String obs_des;


        public Obs(int obs_id, int hike_id, String obs_name, String obs_time,
                   String obs_level, String obs_distance, byte[] obs_img, String obs_des) {
            this.obs_id = obs_id;
            this.hike_id = hike_id;
            this.obs_name = obs_name;
            this.obs_time = obs_time;
            this.obs_level = obs_level;
            this.obs_distance = obs_distance;
            this.obs_img = obs_img;
            this.obs_des = obs_des;
        }

        public int getObs_id() {
            return obs_id;
        }

        public void setObs_id(int obs_id) {
            this.obs_id = obs_id;
        }

        public int getHike_id() {
            return hike_id;
        }

        public void setHike_id(int hike_id) {
            this.hike_id = hike_id;
        }

        public String getObs_name() {
            return obs_name;
        }

        public void setObs_name(String obs_name) {
            this.obs_name = obs_name;
        }

        public String getObs_time() {
            return obs_time;
        }

        public void setObs_time(String obs_time) {
            this.obs_time = obs_time;
        }

        public String getObs_level() {
            return obs_level;
        }

        public void setObs_level(String obs_level) {
            this.obs_level = obs_level;
        }

        public String getObs_distance() {
            return obs_distance;
        }

        public void setObs_distance(String obs_distance) {
            this.obs_distance = obs_distance;
        }

        public byte[] getObs_img() {
            return obs_img;
        }

        public void setObs_img(byte[] obs_img) {
            this.obs_img = obs_img;
        }

        public String getObs_des() {
            return obs_des;
        }

        public void setObs_des(String obs_des) {
            this.obs_des = obs_des;
        }

}
