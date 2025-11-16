interface GalleryProps {
  images?: string[];
}

const Gallery = ({ images = ["/placeholder.svg", "/placeholder.svg", "/placeholder.svg", "/placeholder.svg"] }: GalleryProps) => {
  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
      {images.map((image, index) => (
        <div 
          key={index}
          className="aspect-square overflow-hidden rounded-lg hover:scale-105 transition-transform cursor-pointer"
        >
          <img 
            src={image} 
            alt={`Gallery ${index + 1}`}
            className="w-full h-full object-cover"
          />
        </div>
      ))}
    </div>
  );
};

export default Gallery;
