import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class Ab0l2a6l_BOT extends TelegramLongPollingBot {
    @Override
    public String getBotToken() {
        return "6271537984:AAFLOwUFsoFF7HZXnLjoUgMS3Bm3rosuvwg";
    }

    @Override
    public String getBotUsername() {
        return "ab0l2a6l_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        try {
            if (update.getMessage().hasDocument()) {
                System.out.println("getting document . . . ");
                getDocument(update);
            }
            else if (update.getMessage().hasAudio()) {
                System.out.println("getting audio . . . ");
                getAudio(update);
            }else if (update.getMessage().hasPhoto()) {
                System.out.println("getting photo . . . ");
                getPhoto(update);
            }else if (message.equals("hi")) {
                System.out.println("sending message . . . ");
                sendMessage(update.getMessage().getChatId() , "hi baby");
            } else if (message.equals("id")) {
                System.out.println("sending id . . . ");
                sendMessage(update.getMessage().getChatId(), String.valueOf(update.getMessage().getChatId()));
            } else if (update.getMessage().hasVoice()) {
                System.out.println("getting voice . . . ");
                getVoice(update);
            } else if (message.equals("/list")) {
                System.out.println("getting list . . . ");
                String list = getList();
                System.out.println("sending message . . . ");
                sendMessage(update.getMessage().getChatId() , list);
            } else if (checkNumber(message)) {
                System.out.println("find file . . . ");
                findFiles(Integer.parseInt(message) , update.getMessage().getChatId());
            }
            else {
                System.out.println(message);
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void findFiles(int n , long chatId) throws TelegramApiException {
        File[]files = new File("data").listFiles();
        if (n < 1 || n> files.length){
            sendMessage(chatId , "number input invalid");
        }
        String format = FilenameUtils.getExtension(files[n-1].getName()); // gereftan format file
        System.out.println("sending file . . . ");
        switch (format){
            case "mp3": sendAudio(chatId,files[n - 1]);
            case "tmp" : sendPhoto(chatId , files[n - 1]);
            default: sendDocument(chatId , files[n - 1]);
        }
    }

    public void sendDocument(long chatId , File document) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(new InputFile(document));
        execute(sendDocument);
    }

    public void sendAudio(long chatId , File audio) throws TelegramApiException {
        SendAudio sendAudio = new SendAudio();
        sendAudio.setChatId(chatId);
        sendAudio.setAudio(new InputFile(audio));
        execute(sendAudio);
    }


    public void sendPhoto(long chatId , File photo) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(photo));
        execute(sendPhoto);
    }

    public boolean checkNumber(String text){
        for (int i =0 ; i < text.length() ; i++){
            if (!Character.isDigit(text.charAt(i))){
                return false;
            }
        }
        return true;
    }

     public void sendMessage(long chatId , String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        execute(sendMessage);
    }


    public void getPhoto(Update update) throws TelegramApiException {
        List<PhotoSize> photos = update.getMessage().getPhoto();
        String file_id = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst().orElse(null).getFileId();

        GetFile getFile = new GetFile(file_id);

        String path = execute(getFile).getFilePath();
        File file = downloadFile(path);
        file.renameTo(new File("data/" + file.getName()));
    }

    public void getAudio(Update update) throws TelegramApiException {
        Audio audio = update.getMessage().getAudio();
        String file_id = audio.getFileId();

        GetFile getFile = new GetFile(file_id);

        String path = execute(getFile).getFilePath();
        File file = downloadFile(path);
        file.renameTo(new File("data/" + audio.getFileName()));
    }
    public void getVoice(Update update) throws TelegramApiException {
        Voice voice = update.getMessage().getVoice();
        String file_id = voice.getFileId();

        GetFile getFile = new GetFile(file_id);

        String path = execute(getFile).getFilePath();
        File file = downloadFile(path);
        file.renameTo(new File("data/" +file.getName() ));
    }

    public void getDocument(Update update) throws TelegramApiException {
        Document document = update.getMessage().getDocument();
        String file_id = document.getFileId();

        GetFile getFile = new GetFile(file_id);

        String path = execute(getFile).getFilePath();
        File file = downloadFile(path);
        file.renameTo(new File("data/" + document.getFileName()));
    }
    public String getList(){
        StringBuilder stringBuilder = new StringBuilder();
        String [] strings = new File("data").list();

        if (strings.length == 0) {
            stringBuilder.append("list is empty");
            return stringBuilder.toString();
        }

        for (int i = 0 ; i <strings.length ; i++){
            stringBuilder.append((i+1) + " - " + strings[i]+"\n");
        }
        return stringBuilder.toString();
    }

}
