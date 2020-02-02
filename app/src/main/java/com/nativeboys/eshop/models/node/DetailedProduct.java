package com.nativeboys.eshop.models.node;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DetailedProduct extends Product {

    private String description, details;
    private List<String> hash_tags;

    public DetailedProduct(String product_id, String name, String description, String details, List<String> hash_tags, String price, String upload_time, List<String> gallery_urls, String uploader_id, String category_id, String views_qty, String likes_qty, boolean liked) {
        super(product_id, name, price, upload_time, gallery_urls, uploader_id, category_id, views_qty, likes_qty, liked);
        this.description = description;
        this.details = details;
        this.hash_tags = hash_tags;
    }

    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    public List<String> getHashTags() {
        return hash_tags;
    }

/*    public static DetailedProduct GetDummy() {
        String id = "1";
        String name = "Call of Duty BLACK OPS";
        String description = "Call of Duty is a first-person shooter video game franchise published by Activision. Starting out in 2003, it first focused on games set in World War II, but over time, the series has seen games set in modern times, the midst of the Cold War, futuristic worlds, and outer space. The games were first developed by Infinity Ward, then also by Treyarch and Sledgehammer Games. Several spin-off and handheld games were made by other developers. The most recent title, Call of Duty: Modern Warfare, was released on October 25, 2019.";
        String details = "The series originally focused on the World War II setting, with Infinity Ward developing the first (2003) and second (2005) titles in the series and Treyarch developing the third (2006). Call of Duty 4: Modern Warfare (2007) introduced a new, modern setting, and proved to be the breakthrough title for the series, creating the Modern Warfare sub-series. The game's legacy also influenced the creation of a remastered version, released in 2016. Two other entries, Modern Warfare 2 (2009) and 3 (2011), were made. The sub-series received a soft-reboot with Modern Warfare in 2019. Infinity Ward have also developed two games outside of the Modern Warfare sub-series, Ghosts (2013) and Infinite Warfare (2016). Treyarch made one last World War II-based game, World at War (2008), before releasing Black Ops (2010) and subsequently creating the Black Ops sub-series. Three other entries, Black Ops II (2012), III (2015), and 4 (2018), were made. Sledgehammer Games, who were co-developers for Modern Warfare 3, have also developed two titles, Advanced Warfare (2014) and WWII (2017).";
        List<String> hashTags = new ArrayList<>();
        String price = "85";
        String timestamp = "1580378931";
        List<String> gallery = new ArrayList<>();
        gallery.add("https://c.scdn.gr/images/sku_main_images/014268/14268582/20190214161332_call_of_duty_black_ops_4_pc.jpeg");
        gallery.add("https://c.scdn.gr/images/sku_main_images/018861/18861150/20190603151302_call_of_duty_modern_warfare_ps4.jpeg");
        gallery.add("https://lh3.googleusercontent.com/6lEEhm2WZojAbZ1uqRJb-KEmT24xydDd5I0QjABtlNOeDr9NrNxztXe67AArHUFuqSI");
        gallery.add("https://b.scdn.gr/images/sku_main_images/009008/9008980/20161111130335_call_of_duty_infinite_warfare_pc.jpeg");
        String uploaderId = "";
        String categoryId = "";
        String views = "50";
        String likes = "33";
        boolean liked = true;
        return new DetailedProduct(id, name, description, details, hashTags, price, timestamp, gallery, uploaderId, categoryId, views, likes, liked);
    }*/

    @NonNull
    @Override
    public String toString() {
        return "DetailedProduct{" +
                "description='" + description + '\'' +
                ", details='" + details + '\'' +
                ", hash_tags='" + hash_tags + '\'' +
                ", product_id='" + product_id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", upload_time='" + upload_time + '\'' +
                ", gallery_urls='" + gallery_urls + '\'' +
                ", uploader_id='" + uploader_id + '\'' +
                ", category_id='" + category_id + '\'' +
                ", views_qty='" + views_qty + '\'' +
                ", likes_qty='" + likes_qty + '\'' +
                ", liked=" + liked +
                '}';
    }
}
