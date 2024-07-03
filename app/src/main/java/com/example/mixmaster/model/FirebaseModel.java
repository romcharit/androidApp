package com.example.mixmaster.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.MemoryLruGcSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FirebaseModel {
    FirebaseFirestore db;
    FirebaseStorage storage;
    private FirebaseAuth mAuth;

    // CTOR not public - reachable only from classes in the package
    FirebaseModel(){
        db = FirebaseFirestore.getInstance();

        MemoryCacheSettings memoryCacheSettings = MemoryCacheSettings.newBuilder()
                .setGcSettings(MemoryLruGcSettings.newBuilder()
                        .setSizeBytes(0)
                        .build())
                .build();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(memoryCacheSettings)
                .build();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    public void getAllPostsSince(Long since, Model.Listener<List<Post>> callback){
        db.collection(Post.COLLECTION)
                .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, new Timestamp(since,0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> list = new LinkedList<>();
                if(task.isSuccessful()){
                    QuerySnapshot jsonsList = task.getResult();
                    for (DocumentSnapshot json : jsonsList){
                        // Factory
                        Post post = Post.fromJson(json.getData());
                        list.add(post);
                    }
                }
                callback.onComplete(list);
            }
        });
    }

    public void getAllUsersSince(Long since, Model.Listener<List<User>> callback) {
        db.collection(User.COLLECTION)
                .whereGreaterThan(User.LAST_UPDATED,new Timestamp(since,0))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            List<User> data = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot document:task.getResult()){
                        data.add(User.fromJson(document.getData()));
                    }
                    callback.onComplete(data);
                }
            }
        });
    }

    public void addPost(Post post, Model.Listener<Void> listener){
        db.collection(Post.COLLECTION).document(post.getId()).set(post.toJson()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete(null);
            }
        });
    }

    void uploadImage (String name, Bitmap bitmap, Model.Listener<String> listener)
    {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to 'images/"name".jpg'
        StorageReference imagesRef = storageRef.child("images/" + name + ".jpg");// Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });
    }

    public void getUser(Model.Listener<Boolean> listener){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        listener.onComplete(currentUser!=null);
    }

    public void signUp(User newUser, String password, Model.Listener<Void> listener){
        db.collection(User.COLLECTION).document(newUser.userName).set(User.toJson(newUser))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mAuth.createUserWithEmailAndPassword(newUser.email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(newUser.userName).build();
                                mAuth.getCurrentUser().updateProfile(profile);
                                Model.getInstance().username = newUser.userName;

                                mAuth.signInWithEmailAndPassword(newUser.email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        listener.onComplete(null);
                                        Model.getInstance().refreshAllUsers();
                                    }
                                });
                            }
                        });
            }
        });
    }

    public void logIn(String username, String password, Model.Listener<Boolean> listener) {
        db.collection(User.COLLECTION).document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    User user = User.fromJson(task.getResult().getData());
                    mAuth.signInWithEmailAndPassword(user.email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    listener.onComplete(task.isSuccessful());
                                }
                            });
                } else {
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public void getUserByUsername(String username,Model.Listener<User> listener) {
        db.collection(User.COLLECTION).document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                listener.onComplete(User.fromJson(task.getResult().getData()));
            }
        });
    }

    public boolean isLoggedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser!=null;
    }

    public String getLoggedUserUsername() {
        String username = mAuth.getCurrentUser().getDisplayName();
        return username;
    }

    public void logOut() {
        mAuth.signOut();
    }

    public void deletePost(Post post) {
        db.collection(Post.COLLECTION).document(post.id).delete();
    }

    public void updatePost(Post post, Model.Listener<Void> listener) {
        db.collection(Post.COLLECTION).document(post.id).update(post.toJson()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Model.getInstance().refreshAllPosts();
                listener.onComplete(null);
            }
        });
    }

    public void updateLikedPosts(User user) {
        db.collection(User.COLLECTION).document(user.userName).update(User.toJson(user))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {}
                });
    }

    public void updateUser(User user, Model.Listener<Void> listener) {
        db.collection(User.COLLECTION).document(user.userName).update(User.toJson(user)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Model.getInstance().refreshAllUsers();
                listener.onComplete(null);
            }
        });
    }

    public void isUsernameTaken(String username, Model.Listener<Boolean> listener) {
        db.collection(User.COLLECTION).document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onComplete(task.getResult().getData()!=null);
                }
            }
        });
    }

    public void isEmailTaken(String email, Model.Listener<Boolean> listener) {
        db.collection(User.COLLECTION).whereEqualTo(User.EMAIL,email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onComplete(!task.getResult().getDocuments().isEmpty());
                }
            }
        });
    }

}
