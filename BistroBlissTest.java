package com.example.bistrobliss;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class BistroBlissTest {
    private BistroBliss bistroBliss;

    @Before
    public void setUp() {
        bistroBliss = new BistroBliss();
    }

    @Test
    public void testCheckCredentials_InvalidCredentials() {
        assertFalse(bistroBliss.checkCredentials("nonexistentuser", "password", "admin.txt"));
    }

    @Test
    public void testCreateEmptyFile_FileDoesNotExist() {
        BistroBliss bistroBliss = new BistroBliss();

        String fileName = "testFile.txt";
        bistroBliss.createEmptyFile(fileName);

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        file.delete();
    }

    @Test
    public void testCreateEmptyFile_FileExists() {
        BistroBliss bistroBliss = new BistroBliss();

        String fileName = "testFile.txt";
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bistroBliss.createEmptyFile(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());
        file.delete();
    }
    @Test
    public void testCheckUsernameExists_UsernameExists() {
        File file = new File("testFile.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("user1,password1\n");
            writer.write("user2,password2\n");
            writer.close();
            assertTrue(bistroBliss.checkUsernameExists("user1", file));
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        } finally {
            file.delete();
        }
    }

    @Test
    public void testCheckUsernameExists_UsernameDoesNotExist() {
        File file = new File("testFile.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("user1,password1\n");
            writer.write("user2,password2\n");
            writer.close();

            assertFalse(bistroBliss.checkUsernameExists("user3", file));

        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test");
        } finally {
            file.delete();
        }
    }
}