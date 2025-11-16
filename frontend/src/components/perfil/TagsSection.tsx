import { Badge } from "@/components/ui/badge";

interface TagsSectionProps {
  categories?: string[];
}

const TagsSection = ({ categories = ["CÃES", "Passáros", "PEIXES", "AVES"] }: TagsSectionProps) => {
  return (
    <div className="flex flex-wrap gap-3">
      {categories.map((category, index) => (
        <Badge 
          key={index}
          className="bg-primary hover:bg-primary/90 text-primary-foreground px-6 py-2 text-sm font-medium uppercase rounded-full"
        >
          {category}
        </Badge>
      ))}
    </div>
  );
};

export default TagsSection;
