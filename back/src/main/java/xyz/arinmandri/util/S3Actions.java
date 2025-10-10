package xyz.arinmandri.util;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Utilities;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


/**
 * 원본출처: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/java_s3_code_examples.html#basics
 * 
 * CompletableFuture<Void> future = s3Actions.createBucketAsync( bucketName );
 * future.join();
 */
public class S3Actions
{
	private static final Logger logger = LoggerFactory.getLogger( S3Actions.class );

	private final S3AsyncClient s3AsyncClient;
	private final S3Client s3Client;
	public final S3Utilities utils;

	static public S3Actions getInstance ( String region , String accessKey , String secretKey ) {

		S3Actions i = new S3Actions( region , accessKey , secretKey );

		return i;
	}

	private S3Actions( String region , String accessKey , String secretKey ) {

		s3AsyncClient = getAsyncClient( region, accessKey, secretKey );
		s3Client = getClient( region, accessKey, secretKey );
		utils = s3AsyncClient.utilities();
	}

	private static S3AsyncClient getAsyncClient ( String region , String accessKey , String secretKey ) {

		S3AsyncClient client = S3AsyncClient.builder()
		        .region( Region.of( region ) )
		        .credentialsProvider( StaticCredentialsProvider.create(
		                AwsBasicCredentials.create( accessKey, secretKey ) ) )
		        .build();

		return client;
	}

	private static S3Client getClient ( String region , String accessKey , String secretKey ) {

		S3Client client = S3Client.builder()
		        .region( Region.of( region ) )
		        .credentialsProvider( StaticCredentialsProvider.create(
		                AwsBasicCredentials.create( accessKey, secretKey ) ) )
		        .build();

		return client;
	}

	/**
	 * 로컬 파일을 AWS S3 bucket에 비동기 업로드한다.
	 *
	 * @param bucketName the name of the S3 bucket to upload the file to
	 * @param key        the key (object name) to use for the uploaded file
	 * @param objectPath the local file path of the file to be uploaded
	 * @return a {@link CompletableFuture} that completes with the {@link PutObjectResponse} when the upload is successful, or throws a {@link RuntimeException} if the upload fails
	 */
	public CompletableFuture<PutObjectResponse> uploadLocalFileAsync ( String bucketName , String key , Path objectPath ){

		PutObjectRequest objectRequest = PutObjectRequest.builder()
		        .bucket( bucketName )
		        .key( key )
		        .build();

		CompletableFuture<PutObjectResponse> response = s3AsyncClient.putObject( objectRequest, AsyncRequestBody.fromFile( objectPath ) );
		return response.whenComplete( ( resp , ex )-> {
			if( ex != null ){
				throw new RuntimeException( "Failed to upload file" , ex );
			}
		} );
	}

	/**
	 * 로컬 파일을 AWS S3 bucket에 업로드한다.
	 *
	 * @param bucketName the name of the S3 bucket to upload the file to
	 * @param key        the key (object name) to use for the uploaded file
	 * @param objectPath the local file path of the file to be uploaded
	 * @return a {@link PutObjectResponse} when the upload is successful, or throws a {@link RuntimeException} if the upload fails
	 */
	public PutObjectResponse uploadLocalFile ( String bucketName , String key , Path objectPath ){

		PutObjectRequest objectRequest = PutObjectRequest.builder()
		        .bucket( bucketName )
		        .key( key )
		        .build();

		PutObjectResponse response = s3Client.putObject( objectRequest, objectPath );
		return response;
	}

	/**
	 * Deletes an object from an S3 bucket asynchronously.
	 *
	 * @param bucketName the name of the S3 bucket
	 * @param key        the key (file name) of the object to be deleted
	 * @return a {@link CompletableFuture} that completes when the object has been deleted
	 */
	public CompletableFuture<Void> deleteObjectFromBucketAsync ( String bucketName , String key ) {

		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
		        .bucket( bucketName )
		        .key( key )
		        .build();

		CompletableFuture<DeleteObjectResponse> response = s3AsyncClient.deleteObject( deleteObjectRequest );
		response.whenComplete( ( deleteRes , ex )-> {
			if( deleteRes != null ){
				logger.info( key + " was deleted" );
			}
			else{
				throw new RuntimeException( "An S3 exception occurred during delete" , ex );
			}
		} );

		return response.thenApply( r-> null );
	}

	public DeleteObjectResponse deleteObjectFromBucket ( String bucketName , String key ) {

		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
		        .bucket( bucketName )
		        .key( key )
		        .build();

		DeleteObjectResponse response = s3Client.deleteObject( deleteObjectRequest );
		return response;
	}
}
