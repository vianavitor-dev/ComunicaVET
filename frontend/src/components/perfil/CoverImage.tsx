interface CoverImageProps {
  imageUrl?: string;
}

const CoverImage = ({ imageUrl = "/placeholder.svg" }: CoverImageProps) => {
  return (
    <div className="w-full h-64 md:h-80 overflow-hidden">
      <img 
        src={imageUrl} 
        alt="Cover" 
        className="w-full h-full object-cover"
      />
    </div>
  );
};

export default CoverImage;
