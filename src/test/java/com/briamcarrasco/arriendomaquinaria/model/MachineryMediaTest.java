package com.briamcarrasco.arriendomaquinaria.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MachineryMediaTest {

    private MachineryMedia machineryMedia;
    private Machinery machinery;

    @BeforeEach
    void setUp() {
        machineryMedia = new MachineryMedia();
        machinery = new Machinery();
    }

    @Test
    void testGetSetId() {
        long id = 1L;
        machineryMedia.setId(id);
        assertEquals(id, machineryMedia.getId());
    }

    @Test
    void testGetSetImgUrl() {
        String imgUrl = "/images/machinery1.jpg";
        machineryMedia.setImgUrl(imgUrl);
        assertEquals(imgUrl, machineryMedia.getImgUrl());
    }

    @Test
    void testGetSetVidUrl() {
        String vidUrl = "/videos/machinery1.mp4";
        machineryMedia.setVidUrl(vidUrl);
        assertEquals(vidUrl, machineryMedia.getVidUrl());
    }

    @Test
    void testGetSetMachinery() {
        machinery.setId(1L);
        machinery.setNameMachinery("Excavadora");
        machineryMedia.setMachinery(machinery);

        assertNotNull(machineryMedia.getMachinery());
        assertEquals(machinery, machineryMedia.getMachinery());
        assertEquals("Excavadora", machineryMedia.getMachinery().getNameMachinery());
    }

    @Test
    void testImgUrlCanBeNull() {
        machineryMedia.setImgUrl(null);
        assertNull(machineryMedia.getImgUrl());
    }

    @Test
    void testVidUrlCanBeNull() {
        machineryMedia.setVidUrl(null);
        assertNull(machineryMedia.getVidUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        MachineryMedia media1 = new MachineryMedia();
        media1.setId(1L);
        media1.setImgUrl("/images/test.jpg");
        media1.setVidUrl("/videos/test.mp4");

        MachineryMedia media2 = new MachineryMedia();
        media2.setId(1L);
        media2.setImgUrl("/images/test.jpg");
        media2.setVidUrl("/videos/test.mp4");

        assertEquals(media1, media2);
        assertEquals(media1.hashCode(), media2.hashCode());
    }

    @Test
    void testNotEquals() {
        MachineryMedia media1 = new MachineryMedia();
        media1.setId(1L);
        media1.setImgUrl("/images/test1.jpg");

        MachineryMedia media2 = new MachineryMedia();
        media2.setId(2L);
        media2.setImgUrl("/images/test2.jpg");

        assertNotEquals(media1, media2);
    }

    @Test
    void testToString() {
        machineryMedia.setId(1L);
        machineryMedia.setImgUrl("/images/excavator.jpg");
        machineryMedia.setVidUrl("/videos/excavator.mp4");

        String toString = machineryMedia.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("MachineryMedia"));
    }

    @Test
    void testCreateWithAllFields() {
        MachineryMedia media = new MachineryMedia();
        media.setId(10L);
        media.setImgUrl("/images/bulldozer.jpg");
        media.setVidUrl("/videos/bulldozer_demo.mp4");

        Machinery testMachinery = new Machinery();
        testMachinery.setId(5L);
        testMachinery.setNameMachinery("Bulldozer");
        media.setMachinery(testMachinery);

        assertEquals(10L, media.getId());
        assertEquals("/images/bulldozer.jpg", media.getImgUrl());
        assertEquals("/videos/bulldozer_demo.mp4", media.getVidUrl());
        assertNotNull(media.getMachinery());
        assertEquals(5L, media.getMachinery().getId());
    }

    @Test
    void testEmptyUrls() {
        machineryMedia.setImgUrl("");
        machineryMedia.setVidUrl("");

        assertEquals("", machineryMedia.getImgUrl());
        assertEquals("", machineryMedia.getVidUrl());
        assertTrue(machineryMedia.getImgUrl().isEmpty());
        assertTrue(machineryMedia.getVidUrl().isEmpty());
    }

    @Test
    void testOnlyImageUrl() {
        machineryMedia.setImgUrl("/images/crane.jpg");
        machineryMedia.setVidUrl(null);

        assertEquals("/images/crane.jpg", machineryMedia.getImgUrl());
        assertNull(machineryMedia.getVidUrl());
    }

    @Test
    void testOnlyVideoUrl() {
        machineryMedia.setImgUrl(null);
        machineryMedia.setVidUrl("/videos/crane_operation.mp4");

        assertNull(machineryMedia.getImgUrl());
        assertEquals("/videos/crane_operation.mp4", machineryMedia.getVidUrl());
    }

    @Test
    void testMachineryRelationship() {
        Machinery m = new Machinery();
        m.setId(100L);
        m.setNameMachinery("Grúa Torre");
        m.setStatus("Disponible");

        machineryMedia.setMachinery(m);

        assertNotNull(machineryMedia.getMachinery());
        assertEquals(100L, machineryMedia.getMachinery().getId());
        assertEquals("Grúa Torre", machineryMedia.getMachinery().getNameMachinery());
        assertEquals("Disponible", machineryMedia.getMachinery().getStatus());
    }

    @Test
    void testLongUrls() {
        String longUrl = "https://example.com/very/long/path/" + "segment/".repeat(50) + "file.jpg";
        machineryMedia.setImgUrl(longUrl);
        assertEquals(longUrl, machineryMedia.getImgUrl());
    }

    @Test
    void testInitialValues() {
        MachineryMedia newMedia = new MachineryMedia();
        assertEquals(0L, newMedia.getId());
        assertNull(newMedia.getImgUrl());
        assertNull(newMedia.getVidUrl());
        assertNull(newMedia.getMachinery());
    }

    @Test
    void testMultipleMediaForSameMachinery() {
        MachineryMedia media1 = new MachineryMedia();
        media1.setId(1L);
        media1.setImgUrl("/images/img1.jpg");
        media1.setMachinery(machinery);

        MachineryMedia media2 = new MachineryMedia();
        media2.setId(2L);
        media2.setImgUrl("/images/img2.jpg");
        media2.setMachinery(machinery);

        assertNotEquals(media1.getId(), media2.getId());
        assertEquals(media1.getMachinery(), media2.getMachinery());
    }

    @Test
    void testUrlsWithSpecialCharacters() {
        String imgUrl = "/images/máquina-#1_2024.jpg";
        String vidUrl = "/videos/demostración (1).mp4";

        machineryMedia.setImgUrl(imgUrl);
        machineryMedia.setVidUrl(vidUrl);

        assertEquals(imgUrl, machineryMedia.getImgUrl());
        assertEquals(vidUrl, machineryMedia.getVidUrl());
    }
}
